package allinone.business;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.AuditRegisterEntity;
import allinone.data.CountryEntity;
import allinone.data.ResponseCode;
import allinone.data.SimpleException;
import allinone.databaseDriven.UserDB;
import allinone.databaseDriven.UserInfoDB;
import allinone.protocol.messages.RegisterRequest;
import allinone.protocol.messages.RegisterResponse;
import allinone.server.Server;
import vn.game.common.MD5;

public class RegisterBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(RegisterBusiness.class);
    private static ConcurrentHashMap<String, AuditRegisterEntity> auditRegister = new ConcurrentHashMap<String, AuditRegisterEntity>();
    private static final int MAX_TIME = 60000; 	// 1 minutes moi lan dang ki
    private static final int MAX_USER = 10; 		// toi da 5 nick
    private static final int MAX_USER_BY_IP = 30;
    private static final int MAX_USER_DATE = 86400000; // one day
    private static final int MONEY_REGIST = 500;

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) throws ServerException {
        int rtn = PROCESS_FAILURE;
        mLog.debug("[REGISTER] : Catch");

        MessageFactory msgFactory = aSession.getMessageFactory();

        RegisterResponse resRegister = (RegisterResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try {

            RegisterRequest rqRegister = (RegisterRequest) aReqMsg;
            String username = rqRegister.mUsername;
            String password = rqRegister.mPassword;
            String phone = rqRegister.phone;
            int deviceType = rqRegister.deviceType;
            String device = rqRegister.device;
            String deviceID = rqRegister.deviceUID;
            int partnerId = rqRegister.partnerId;

            // default set
            //aSession.setByteProtocol(0);
            aSession.setByteProtocol(AIOConstants.PROTOCOL_REFACTOR);
            resRegister.session = aSession;

            if (rqRegister.mPassword == null || rqRegister.mUsername == null || rqRegister.mPassword.equals("")) {
                throw new SimpleException("Bạn cần phải nhập đầy đủ thông tin đăng ký!");
            }

            if (rqRegister.mUsername.length() < 6) {
                throw new SimpleException("Tài khoản phải nhiều hơn 5 ký tự, không có khoảng trống, không được toàn chữ số!");
            }
            if (rqRegister.mUsername.length() > 20) {
                throw new SimpleException("Tài khoản không được quá 20 ký tự!");
            }
            if (checkInvalidName(rqRegister.mUsername)) {
                throw new SimpleException("Tên tài khoản không hợp lệ!");
            }

            if (rqRegister.mPassword.length() < 8) {
                throw new SimpleException("Mật khẩu phải nhiều hơn 8 ký tự!");
            }
            if (rqRegister.mPassword.equals("12345678")) {
                throw new SimpleException("Mật khẩu quá dễ.");
            }
//            try
//            {            	
            long currentTime = System.currentTimeMillis();

            // loai bo key cu
            resetAuditRegister(currentTime);

//                // khong duoc phep dang ki lai ngay
            if (aSession.getLastRegister() > 0 && currentTime - aSession.getLastRegister() < MAX_TIME) {
                throw new SimpleException("Bạn vui lòng chờ đăng kí lại sau ít phút!");
            }

            String ip = aSession.getOnlyIP().replace("/", "");
            String clientSessionKey = ip; // su dung IP lam key
            if (deviceType >= 3) {// 1 ios, 2 android, 3 khac thi lay theo deviceid
                clientSessionKey = deviceID; // su dung deviceID lam key
            }
            //mLog.debug("clientSessionKey:"+clientSessionKey);
            if (auditRegister.containsKey(clientSessionKey)) {
                AuditRegisterEntity lastEntity = auditRegister.get(clientSessionKey);

                mLog.debug("So lan da dang ki " + lastEntity.getCount());

                if (lastEntity.getCount() >= MAX_USER) {

                    throw new SimpleException("Số tài khoản đăng kí quá nhiều, bạn vui lòng thử lại sau!");
                }

                // set data
                lastEntity.setCount(lastEntity.getCount() + 1);
                lastEntity.setLastDateTime(currentTime);
            } else {
                mLog.debug(" Dang ki lan dau tien " + aSession.getOnlyIP());
                AuditRegisterEntity lastEntity = new AuditRegisterEntity(1, currentTime);
                auditRegister.put(clientSessionKey, lastEntity);
            }

            // validate name
            Pattern p = Pattern.compile("^[1-9]\\d{0,}+$");
            if (p.matcher(rqRegister.mUsername).matches()) {
                throw new SimpleException("Tài khoản không được toàn chữ số (trừ bắt đầu bằng 0)!");
            }

            String pattern = "^[a-zA-Z_0-9.@_]{5,100}$";
            if (!rqRegister.mUsername.matches(pattern)) {
                throw new SimpleException("Tài khoản không được chứa ký tự đặc biệt!");
            }

            UserDB userDb = new UserDB();
            int totalDeviceID = userDb.checkDeviceID(deviceID);
            int totalIp = userDb.countByIP(ip);
            mLog.debug(deviceID + " totalDeviceID:" + totalDeviceID);
            mLog.debug(ip + " totalIp:" + totalIp);
            if (totalDeviceID >= MAX_USER) {
                throw new SimpleException("Thiết bị này đã đăng ký quá " + MAX_USER + " tài khoản!");

            }
//            else if(totalIp >= MAX_USER_BY_IP){
//                throw new SimpleException("Thiết bị này đã đăng ký quá "+MAX_USER_BY_IP+" tài khoản!");
//            }
            long uid = 0;
            long tempUid = 0;

//check user
            boolean checkUserName = userDb.userIsExist(username);
            if (checkUserName) {

                throw new SimpleException("Tài khoản đã tồn tại, vui lòng đăng ký tài khoản khác!");

            }
            String countryCode = "vn";
            try {
                    CountryEntity countryObj = new CountryEntity();
                    countryCode = countryObj.getCountryByIP(ip);
                   
                } catch (Exception e) {
                    mLog.debug("Excpetion "+e.getMessage());
                }
            uid = userDb.registerUserMxh(username, MD5.toMD5(password), 0, phone, partnerId, false, 0, false, rqRegister.refCode, 1, rqRegister.deviceUID, 0, deviceType, ip, aSession.getRealMoney(),countryCode);
//            } else {
//            	tempUid = userDb.registerUserTempKey(username, password, 0, partnerId,false,0,false,rqRegister.refCode,1, deviceType);            	
//            	uid = -3;
//            }

            if (uid == -1 || tempUid == -1) {
                resRegister.setFailure(ResponseCode.FAILURE, "Tài khoản đã tồn tại, vui lòng đăng ký tài khoản khác!");
            } else if (uid > 0) {

                aSession.setLastRegister(System.currentTimeMillis());
                StringBuilder sb = new StringBuilder();
                sb.append("Chúc mừng bạn đăng ký thành công");
                resRegister.values = sb.toString();
                resRegister.setSuccess(ResponseCode.SUCCESS, uid, MONEY_REGIST, 1, 1);
                mLog.debug("[REGISTER] : " + username + " Success");
                //xy ly created user nodebb
//                if (Server.isNodeBB.length() > 5) {
//                    mLog.debug("vao tao tk bb");
//                    NodeBBBusiness.createUserNodeBB(uid, username, password);
//                }

                //end call xu ly nodebb
                //insert users_info
                try {
                    UserInfoDB InfoDBObj = new UserInfoDB();
                    mLog.debug("countryCode:" + countryCode);
                    InfoDBObj.insert(uid, countryCode);
                } catch (Exception e) {
                    mLog.debug("Excpetion " + e.getMessage());
                }
                //end insert user_info
            } else {
                resRegister.setFailure(ResponseCode.FAILURE, "Không thể đăng ký vui lòng thử lại sau");
            }

            rtn = PROCESS_OK;
        } catch (SimpleException se) {
            resRegister.setFailure(ResponseCode.FAILURE, se.msg);
            rtn = PROCESS_OK;
            mLog.debug("[REGISTER] : " + se.msg);
        } catch (Throwable t) {
            resRegister.setFailure(ResponseCode.FAILURE, "Dữ liệu bạn nhập không chính xác!");
            rtn = PROCESS_OK;
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally {
            if ((resRegister != null) && (rtn == PROCESS_OK)) {
                aResPkg.addMessage(resRegister);
            }
        }
        return rtn;
    }

    // moi ngay tren 1 IP chi duoc phep dang ki toi da Max nick
    private void resetAuditRegister(long currentTime) {
        Set<String> keys = this.auditRegister.keySet();
        // sau 24 cho phep dang ki lai, xoa bo bot data
        for (String key : keys) {
//    		mLog.debug(" Key " + key);    		
            AuditRegisterEntity ar = this.auditRegister.get(key);
//    		mLog.debug(" Last regist  " + ar.getLastDateTime());
            if (currentTime - ar.getLastDateTime() > this.MAX_USER_DATE) {
//    			mLog.debug(" Remove key  ");
                this.auditRegister.remove(key);
            }
        }
    }

    private boolean checkInvalidName(String name) {
        String[] samples = {"admjn", "admin", "quanly", "tuquy", "hethong", "fuck", "guest", "facebook"};
        int len = samples.length;
        for (int i = 0; i < len; i++) {
            String sample = samples[i].toLowerCase();
            if (name.toLowerCase().contains(sample)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        String password = "23423423423423421295287|84906123106|9029|NAP|GTS CM NAP5 X117837|VMS|1|2018-01-08 11:15:50|1|23aasd22#4@1490kdj34&^45u234";

        System.out.println("pass:" + MD5.toMD5(password));
        // validate name
//        String parten =  "^0[0-9]{9,10}$";
//        String phone = "013722611616";
//        if(phone.matches(parten))
//        {
//        	System.out.println("Match");
//        } else {
//        	System.out.println("Not Match");
//        }
    }
}
