package allinone.business;

import java.util.List;
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
import allinone.data.ChargingInfo;
import allinone.data.QueueUserEntity;
import allinone.data.ResponseCode;
import allinone.data.SimpleException;
import allinone.databaseDriven.InfoDB;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.RegisterRequest21;
import allinone.protocol.messages.RegisterResponse21;
import allinone.queue.data.VMGQueue;

public class RegisterBusiness21 extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(RegisterBusiness21.class);
    
    private static final int NEW_PROTOCOL=1;
    private static final int MAX_TIME = 60000;
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) throws ServerException {

        int rtn = PROCESS_FAILURE;
        mLog.debug("[REGISTER] : Catch");

        MessageFactory msgFactory = aSession.getMessageFactory();

        RegisterResponse21 resRegister =
                (RegisterResponse21) msgFactory.getResponseMessage(aReqMsg.getID());
        try {
            RegisterRequest21 rqRegister = (RegisterRequest21) aReqMsg;
            String username = rqRegister.mUsername;
            String password = rqRegister.mPassword;
            String phone = rqRegister.phone;
            String mail = rqRegister.mail;
            String key = rqRegister.key;
            int age = rqRegister.mAge;
            int sex = 0;
            int partnerId = rqRegister.partnerId;
            aSession.setByteProtocol(rqRegister.protocol);
            
            resRegister.session = aSession;
            
            if(rqRegister.mPassword == null || rqRegister.mUsername == null || rqRegister.mPassword.equals(""))
            {
                throw new SimpleException("Ban can phai nhap du lieu");
                
            }
            if(rqRegister.mUsername.length()< 3)
            {
                throw new SimpleException("Tài khoản phải lớn hơn 3 ký tự");
            }
            
            if(rqRegister.mPassword.length() <6)
            {
                throw new SimpleException("Mật khẩu phải lớn hơn 6 ký tự");
            }
            
            if(aSession.getLastRegister() > 0 && System.currentTimeMillis() - aSession.getLastRegister() < MAX_TIME)
            {
                throw new SimpleException("Xin bạn thử đăng ký lại sau");
            }
            
            
            Pattern p = Pattern.compile("^[1-9]\\d{0,}+$");
            if(p.matcher(rqRegister.mUsername).matches())
            {
                throw new SimpleException("Tài khoản không được toàn chữ số(trừ bắt đầu bằng 0)");
            }
            
            String pattern = "^[a-zA-Z_0-9.@_,-]{3,100}$";
            
            
            if(!rqRegister.mUsername.matches(pattern))
            {
                throw new SimpleException("Tài khoản không được chứa ký tự đặc biệt");
            }
            
            
            if (rqRegister.isMale) {
                sex = 1;
            }
            
            UserDB userDb = new UserDB();
            
            if(rqRegister.partnerId == AIOConstants.VMG_PARTNER_ID)
            {
                //hello
                VMGQueue vmgQueue = new VMGQueue();
                QueueUserEntity entity = new QueueUserEntity(rqRegister, aSession, resRegister);
                vmgQueue.insertUser(entity);
                return 1;
            }
            
            
            long uid = -1;
            if(rqRegister.protocol< AIOConstants.PROTOCOL_MXH)
            {
                     
                  uid =    userDb.registerUser(username, password, age, sex, mail, phone, key, partnerId);
            }
            else
            {
//                mLog.debug("gioithieu uid " + rqRegister.gioiThieuUid + " mxh " + rqRegister.isMxh);
//                 uid =    userDb.registerUserMxh(username, password, sex, phone, partnerId
//                            , rqRegister.partnerId == AIOConstants.VMG_PARTNER_ID, rqRegister.gioiThieuUid,rqRegister.isMxh, rqRegister.refCode,0 );
            }
            
             if(uid == -1)
             {
                 resRegister.setFailure(ResponseCode.FAILURE, "Tài khoản đã có người đăng ký, bạn vui lòng đăng ký tài khoản khác!");
             }
             else
             {
                 aSession.setLastRegister(System.currentTimeMillis());
                resRegister.setSuccess(ResponseCode.SUCCESS, uid, 3000, 1, 1);
                
                //for active
                try
                {
                    InfoDB db = new InfoDB();
                    List<ChargingInfo> partnerCharging = db.getPartnerChargings(rqRegister.partnerId, rqRegister.refCode);
                    
//                    String chooseActive = "viettel";
//                    String ip=aSession.getIP();
//                     if(ip.matches(AIOConstants.MOBIFONE_PATTERN_IP1)||ip.matches(AIOConstants.MOBIFONE_PATTERN_IP2)|ip.matches(AIOConstants.MOBIFONE_PATTERN_IP3))
//                         chooseActive = "mobifone";
//
//                     if(ip.matches(AIOConstants.VINAPHONE_PATTERN_IP))
//                         chooseActive = "vinaphone";
//                                     
//                     int chargingSize = partnerCharging.size();
                     ChargingInfo activeInfo = null;
//                     for(int i = 0; i< chargingSize; i++)
//                     {
//                         ChargingInfo info = partnerCharging.get(i);
//                         if(info.isActive)
//                         {
//                             activeInfo = info;
//                             if(info.desc.contains(chooseActive))
//                                break;
//                         }
//                     }
                     
                      activeInfo = db.getActive(partnerCharging, null);

                     if(activeInfo != null && activeInfo.isNeedActive)
                     {
                          StringBuilder sb = new StringBuilder();
                          sb.append(activeInfo.value).append(" "). append(Long.toString(uid)).append(" ").append(activeInfo.number);
//                             sb.append(info.number).append(AIOConstants.SEPERATOR_BYTE_1); //dau so
//                             sb.append(info.value).append(AIOConstants.SEPERATOR_BYTE_1);//ma nap
//                             sb.append(Long.toString(uid));

                          resRegister.values = sb.toString();

                     }
                                         
                     
//                     if(chargingSize>3)
//                     {
//                        
//                         
//                         ChargingInfo info = partnerCharging.get(3);
//                         if(info.isNeedActive)
//                         {
//                             sb.append(info.value).append(" "). append(Long.toString(uid)).append(" ").append(info.number);
////                             sb.append(info.number).append(AIOConstants.SEPERATOR_BYTE_1); //dau so
////                             sb.append(info.value).append(AIOConstants.SEPERATOR_BYTE_1);//ma nap
////                             sb.append(Long.toString(uid));
//
//                             resRegister.values = sb.toString();
//                         }
//                     }
                                     
                }
                catch(Exception ex)
                {
                    
                }
                            
                
                mLog.debug("[REGISTER] : " + username + " Success");
             }
             
            rtn = PROCESS_OK;
        }
        catch(SimpleException se)
        {
            
            resRegister.setFailure(ResponseCode.FAILURE, se.msg);
            rtn = PROCESS_OK;
            mLog.debug("[REGISTER] : " + se.msg);
        }                    
        catch (Throwable t) {
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
}
