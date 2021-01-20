package allinone.business;

import java.sql.SQLException;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.protocol.messages.ExpiredSessionResponse;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.CountryEntity;

import allinone.data.MessagesID;
import allinone.data.QueueEntity;
import allinone.data.ResponseCode;
import allinone.data.SimpleTable;
import allinone.data.UserEntity;

import allinone.data.ZoneID;

import allinone.databaseDriven.UserDB;

import allinone.databaseDriven.SessionDB;
import allinone.databaseDriven.UserInfoDB;
import allinone.protocol.messages.CancelRequest;
import allinone.protocol.messages.LogoutRequest;
import allinone.protocol.messages.FastLoginRequest;
import allinone.protocol.messages.FastLoginResponse;

import allinone.queue.data.CommonQueue;
import allinone.server.Server;

import org.apache.commons.lang.RandomStringUtils;
import vn.game.common.MD5;
import vn.game.common.sendCommon;
import vn.game.db.DBException;

public class FastLoginBusiness extends AbstractBusiness {

    private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(FastLoginBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        int rtn = PROCESS_FAILURE;
        mLog.debug("[SOCIAL LOGIN]: Catch" + "messageID:" + aReqMsg.getID());
        MessageFactory msgFactory = aSession.getMessageFactory();
        sendCommon.sendConfigGame(aSession, msgFactory);//config_game
        FastLoginResponse resLogin = (FastLoginResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        FastLoginRequest rqLogin = (FastLoginRequest) aReqMsg;

        // check user current login
        if (aSession.isLoggedIn()) {
            LogoutRequest reqLogout = (LogoutRequest) msgFactory.getRequestMessage(MessagesID.LOGOUT);
            IResponsePackage responsePkg = aSession.getDirectMessages();
            IBusiness business = msgFactory.getBusiness(MessagesID.LOGOUT);
            try {
                business.handleMessage(aSession, reqLogout, responsePkg);
            } catch (ServerException e) {
                mLog.error("Logout error " + e.getMessage());
            }
        }

        UserDB userDb = new UserDB();

        SessionDB sdb = new SessionDB();
        try {
            UserEntity user = null;
            resLogin.session = aSession;
            aSession.setByteProtocol(AIOConstants.PROTOCOL_REFACTOR);
            resLogin.numberOnline = Server.numberOnline;
            aSession.setMobileDevice(true);

            aSession.setTopplayVersion(10);
            //resLogin.isMobile = true;
            String version = rqLogin.mobileVersion;
            int deviceType = rqLogin.deviceType;
            String deviceID = rqLogin.deviceID;
            if (deviceID.equals("")) {
                throw new Exception("Thiết bị của bạn không hỗ trợ chức năng này!.");
            } else if (deviceID.length() <= 5) {
                throw new Exception("Thiết bị của bạn không hỗ trợ chức năng này.");
            }

//            if (!rqLogin.device.equals("android") || rqLogin.device.equals("ios")) {
//                throw new Exception("Thiết bị của bạn không hỗ trợ chức năng này!.");
//            } 
            try {
                user = userDb.getUserByDeviceID(deviceID, aSession.getRealMoney());
            } catch (SQLException ex) {
                mLog.debug("Login Loi database " + ex.getMessage());
                throw new Exception("Hệ thống tạm thời bảo trì, vui lòng thử lại sau!");
            }

            if (user.mUid <= 0) // non-existed user
            {
                mLog.debug(" user null");
                long currentTime = System.currentTimeMillis();
                String pass = MD5.toMD5("" + currentTime);
                String username = "now_" + currentTime;
                String viewname = RandomStringUtils.random(3, true, true).toUpperCase();
                String[] ips = aSession.getIP().split(":");
                String ip = aSession.getIP();
                if (ips.length > 0) {
                    ip = ips[0].replace("/", "");
                }
                String countryCode = "vn";
                try {
                    CountryEntity countryObj = new CountryEntity();
                    countryCode = countryObj.getCountryByIP(ip);

                } catch (Exception e) {
                    mLog.debug("Excpetion " + e.getMessage());
                }
                long uid = userDb.registerUserMxh(username, pass, 0, "", 0, false, 0, false, "", 1, deviceID, 2, deviceType, ip, aSession.getRealMoney(), countryCode);
                if (uid > 0) {
                    //update viewname and username
                    username = "now_" + uid;
                    viewname = uid + "_" + viewname;
                    try {
                        int result = userDb.changeViewNameByFastLogin(uid, viewname, username);
                    } catch (DBException ex) {
                        mLog.debug("error update viewname fasstlogin", ex);
                    } catch (SQLException ex) {
                        mLog.debug("error update viewname fasstlogin SQLException", ex);
                    }
                    //insert users_info
                    try {
                        UserInfoDB InfoDBObj = new UserInfoDB();
                        mLog.debug("countryCode:" + countryCode);
                        InfoDBObj.insert(uid, countryCode);
                    } catch (Exception e) {
                        mLog.debug("Excpetion " + e.getMessage());
                    }
                    //end insert user_info
                    user = userDb.getUserInfoFullMoney(uid);
                }

            }
            mLog.debug(" user uid " + user.mUsername);
            if (user != null && user.mUid > 0) {

                if (user.isLock == 1) {
                    throw new Exception("Tài khoản của bạn đã bị khóa!");
                }

                // kiem tra xem user co dang choi trong ban nao khong
                String alertMessage = this.checkPlayInMatch(resLogin, user.mUid, aSession);

                user.isLogin = true;

                aSession.setUID(user.mUid);
                resLogin.setSuccess(ResponseCode.SUCCESS, user.mUid, user.money,user.realmoney, user.lastLogin, user.avatarID, user.viewName);
                resLogin.expr = user.experience;
                resLogin.isActive = user.isActive;
                resLogin.password = user.mPassword;

                mLog.debug(" user phone " + user.cellPhone + " phone update " + resLogin.isPhoneUpdate);
                resLogin.isPhoneUpdate = false;
                if (user.cellPhone == null || "".equals(user.cellPhone)) {
                    resLogin.isPhoneUpdate = true;
                }

                boolean checkNewMail = false;
                String mailContent = "";
                String mailTitle = "";
                boolean checkNewEvent = false;

                resLogin.chkEmail = checkNewMail;
                resLogin.chkEvent = checkNewEvent;

//                resLogin.alertEmailContent = mailContent;
//                resLogin.alertEmailTitle = mailTitle;                                
                if (!"".equals(alertMessage)) {
                    resLogin.alertEmailContent = alertMessage;
                    resLogin.alertEmailTitle = "Cảnh báo";
                } else {
                    resLogin.alertEmailContent = mailContent;
                    resLogin.alertEmailTitle = mailTitle;
                }

                resLogin.usrEntity = user;
                aSession.setUserName(user.mUsername);
                aSession.setLoggedIn(true);
                aSession.setUserEntity(user);
                if (user.realmoney <= 0) {
                    aSession.setRealMoney(AIOConstants.PRIFIX_REALMONEY);
                }
                // for process ITUNES public
//                if("9603".equals(rqLogin.refCode)) {
//                	resLogin.expr = 9603;                	
//                }
                CommonQueue queue = new CommonQueue();
                QueueEntity entity = new QueueEntity(aSession, resLogin);
                queue.insertQueue(entity);
                //add by zep
                sdb.addSessionDB(user.mUid, aSession.getID(), aSession.getUserName(), aSession.getOnlyIP(), "");
                sendCommon.sendCurrentSessionMoney(aSession, msgFactory);
                //sendCommon.sendZoneConfig(aSession, msgFactory);
                sendCommon.sendCountEmail(aSession, msgFactory);
                //sendCommon.sendMsgSystem(aSession, msgFactory);
                sendCommon.sendNumberOnline(aSession, Server.numberOnline + 1);
                rtn = PROCESS_OK;
            } else {
                resLogin.setFailure(ResponseCode.FAILURE, "Không để đăng nhập.");
            }

        } catch (LoginException ex) {
            resLogin = null;
            aSession.setLoggedIn(false);
            aSession.setCommit(false);
            try {
                userDb.logout(aSession.getUID(), "");

            } catch (SQLException ex1) {
                mLog.error(ex.getMessage(), ex1);
            }
            try {
                sdb.updateSessionDB(aSession.getUID(), "", "", "", "");
            } catch (Exception e) {

            }
            rtn = PROCESS_OK;
            mLog.warn(ex.getMessage());

        } catch (BusinessException ex) {
            resLogin.setFailure(ResponseCode.FAILURE, ex.getMessage());
            aSession.setLoggedIn(false);
            aSession.setCommit(false);

            try {
                userDb.logout(aSession.getUID(), "");
            } catch (SQLException ex1) {
                mLog.error(ex.getMessage(), ex1);
            }
            try {
                sdb.updateSessionDB(aSession.getUID(), "", "", "", "");
            } catch (Exception e) {

            }
            rtn = PROCESS_OK;

        } catch (Exception ex) {
            resLogin.setFailure(ResponseCode.FAILURE, ex.getMessage());
            aSession.setLoggedIn(false);
            aSession.setCommit(false);
            rtn = PROCESS_OK;
        } finally {
            if ((resLogin != null)) {
                aResPkg.addMessage(resLogin);
            }
        }
        return rtn;
    }

    public String checkPlayInMatch(FastLoginResponse resLogin, long uid, ISession aSession) {

        int currentZone = 0;
        long currentMatchId = 0;
        String alertMessage = "";
        int tableIndex = 0;
        String zoneName = "";
        boolean isPlaying = false;

        try {

            // Kiem tra xem co dang ton tai trong ban choi game nao khong
            SimpleTable table = SimpleTable.findUserInTable(uid);

            if (table != null) {
                currentMatchId = table.getMatchID();
                currentZone = table.getNotNullSession().getCurrentZone();
                zoneName = ZoneID.getZoneName(currentZone);
                isPlaying = table.isPlayerPlaying(uid);
                tableIndex = table.getTableIndex();
            }

            mLog.debug(" In Match " + currentMatchId + " isplaying " + isPlaying);

            mLog.debug(" New session " + aSession.getID() + " " + aSession.getUID());

            ISession temp = aSession.getManager().findSession(uid);

            if (temp != null) {
                mLog.debug(" Old session " + temp.getID() + " " + temp.getUID());
            }

            if (temp != null && temp.getID() != aSession.getID() && !temp.realDead() && !temp.isExpired() && temp.isLoggedIn()) {

                try {
                    if (currentMatchId > 0 && !isPlaying) {

                        IBusiness business = aSession.getMessageFactory().getBusiness(MessagesID.MATCH_CANCEL);
                        CancelRequest rqMatchCancel = (CancelRequest) aSession.getMessageFactory().getRequestMessage(MessagesID.MATCH_CANCEL);
                        rqMatchCancel.uid = temp.getUID();
                        rqMatchCancel.mMatchId = currentMatchId;
                        rqMatchCancel.isLogout = true;
                        rqMatchCancel.isSendMe = false;

                        try {
                            business.handleMessage(temp, rqMatchCancel, temp.getDirectMessages());
                        } catch (ServerException se) {
                            this.mLog.error("cancelTable " + se.getCause());
                        } catch (Throwable e) {
                            this.mLog.error("cancelTable " + e.getCause());
                        }

                    }

                    // tao connection moi, xoa connection cu, neu dang o trong ban thi reconnect lai vao ban                    
                    ExpiredSessionResponse expiredSession = (ExpiredSessionResponse) temp.getMessageFactory().getResponseMessage(9999);
                    expiredSession.mErrorMsg = "Vui lòng đăng nhập lại.";
                    temp.write(expiredSession);

                    temp.setLoggedIn(false);
                    temp.setUIDNull();

//	            	temp.close();
                } catch (ServerException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // danh cho nhung user vao day thang khac ra ngoai, han che reconnect lai luon
//	        	aSession.setTopplayVersion(1000);
            } else {

                if (currentMatchId > 0 && !isPlaying) {
                    table.removePlayerNullSession(uid);
                }
            }

            aSession.setCurrentZone(currentZone);

            if (currentMatchId > 0 && isPlaying) {
                alertMessage = "Bạn đang chơi dở game " + zoneName + " bàn số " + tableIndex + ", vui lòng vào lại bàn chơi tiếp, tránh bị trừ tiền nếu thua!";
            } else {
                currentMatchId = 0;
                currentZone = 0;
            }

            //mLog.debug("Login join table " + currentZone + "  " + currentMatchId);
            resLogin.zone_id = currentZone;
            resLogin.lastRoom = currentMatchId;

        } catch (Exception ex) {
            mLog.equals(" Login check table error " + ex.getMessage());
        }

        return alertMessage;
    }

}
