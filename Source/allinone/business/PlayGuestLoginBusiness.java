package allinone.business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;

import tools.CacheEventInfo;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.Mail;
import allinone.data.QueueEntity;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.databaseDriven.InfoDB;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.PlayGuestLoginRequest;
import allinone.protocol.messages.PlayGuestLoginResponse;
import allinone.queue.data.CommonQueue;
import allinone.server.Server;
import vn.game.common.sendCommon;

public class PlayGuestLoginBusiness extends AbstractBusiness {

    private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(PlayGuestLoginBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        int rtn = PROCESS_FAILURE;

//        mLog.debug("[Guest LOGIN]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        PlayGuestLoginResponse resLogin = (PlayGuestLoginResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        PlayGuestLoginRequest rqLogin = (PlayGuestLoginRequest) aReqMsg;
        sendCommon.sendConfigGame(aSession, msgFactory);//config_game
        UserDB userDb = new UserDB();

        try {

            UserEntity user;
            aSession.setByteProtocol(AIOConstants.PROTOCOL_REFACTOR);
            resLogin.session = aSession;
            resLogin.numberOnline = Server.numberOnline;

            aSession.setMobileDevice(true);
            if (rqLogin.mobileVersion != null) {
                if (rqLogin.mobileVersion.toUpperCase().contains("ANDROID")) {
                    mLog.debug("android device");
                    aSession.setDeviceType(AIOConstants.ANDROID_DEVICE);

                } else if (rqLogin.mobileVersion.toUpperCase().contains("IPHONE")) {
                    mLog.debug("iphone device");
                    aSession.setDeviceType(AIOConstants.IPHONE_DEVICE);

                } else {
                    aSession.setDeviceType(AIOConstants.MOBILE_DEVICE);
                }

            }

            String[] clientArr = rqLogin.mobileVersion.split("\\.");

            try {
                int codeVersion = Integer.parseInt(clientArr[0]);
                mLog.debug(" Code version " + codeVersion);
                if (codeVersion >= 5) {
                    aSession.setTopplayVersion(codeVersion);
                }
            } catch (Exception ex) {
                mLog.debug(" Mobile version khogn dung format ");
            }

            resLogin.isMobile = true;
            //user = userDb.guestLogin(rqLogin.deviceUId, rqLogin.partnerId, rqLogin.refCode, rqLogin.mobileVersion, rqLogin.deviceType);
            user = null;
            if (user == null) // non-existed user
            {
                if (aSession.getRetryLogin(rqLogin.deviceUId) > AIOConstants.MAX_RETRY_LOGIN) {
                    throw new LoginException("Không thể đăng nhập");
                }
                aSession.setRetryLogin(rqLogin.deviceUId);
                mLog.warn("***Khong the dang nhap voi deviceUid " + rqLogin.deviceUId);
                throw new Exception("Không thể đăng nhập");

            } else {

                if (user.isLock == 1) {
                    throw new Exception("Tài khoản của bạn đã bị khóa!");
                }else if(user.utype == 7 || user.utype == 8){
                    throw new Exception("Tài khoản đại lý không đăng nhập được!");
                }

                mLog.warn("Check is logined" + user.isOnline);
                ISession temp = aSession.getManager().findSession(user.mUid);

                if (temp != null) {
                    mLog.warn("Session value " + temp.getUserName());
                    mLog.warn("Session value " + temp.realDead());
                    mLog.warn("Session value " + System.currentTimeMillis());
                    mLog.warn("Session value " + temp.getLastAccessTime().getTime());
                } else {
                    mLog.warn("Session value null");
                }

                if (temp != null && (temp.getUserName() != null && !temp.getUserName().equals("")) && !temp.realDead() && !temp.isExpired() && temp.isLoggedIn()) {
                    mLog.warn("[UserName] is logged " + user.mUsername);
                    temp.setLastAccessTime(new Date());
                    throw new Exception("Tài khoản này đã đăng nhập!");
                } else {
                    //TODO: kick out current session
//	            	if(temp != null) {
//	                	try {
//		                    if(temp.getRoom() != null) {
//		                  	  temp.cancelTable();
//		                     }
//		                 } catch (Exception ex) {
//		                    mLog.error("close idle connection");
//		                }
//		            	temp.sessionClosed();
//	            	}

                    user.isLogin = true;

//                	CacheUserInfo cache = new CacheUserInfo();
//                	cache.updateCacheUserInfo(user);
                }

                boolean checkNewMail = false;
                String mailContent = "";
                String mailTitle = "";
                boolean checkNewEvent = false;

                try {
                    // check new mail
                    CacheEventInfo cache = new CacheEventInfo();
                    ArrayList<Mail> dataList = (ArrayList<Mail>) cache.getMail(user.mUid);

                    int size = dataList.size();
                    for (int i = 0; i < size; i++) {
                        Mail mail = dataList.get(i);
                        if (mail.isRead == 0) {
                            checkNewMail = true;
                        }
                    }

                    if (checkNewMail) {
                        // get alert mail content
                        InfoDB db = new InfoDB();
                        Mail mail = db.getAlertMail(user.mUid);
                        mailContent = mail.detail;
                        mailTitle = mail.title;
                        db.readAlertMail(mail.id, user.mUid);
                    }

                    // check new event
//	                List<EventEntity> lstEvents = EventDB.getEvents(0);
//	                size = lstEvents.size();
//	                for (int i = 0; i < size; i++) {
//	                	EventEntity EventEntity = lstEvents.get(i);
//	                	if(EventEntity.isConcurrent()) {
//	                		checkNewEvent = true;
//	                	}
//	                }
                } catch (Exception ex) {
                    mLog.equals(" Error get email and event " + ex.getMessage());
                }

                resLogin.chkEmail = checkNewMail;
                resLogin.chkEvent = checkNewEvent;
                resLogin.alertEmailContent = mailContent;
                resLogin.alertEmailTitle = mailTitle;

                aSession.setUID(user.mUid);
                aSession.setuType(user.utype);
                resLogin.setSuccess(ResponseCode.SUCCESS, user.mUid, user.money, user.lastLogin);

//                int partnerId = user.partnerId;
//                resLogin.chargingInfo = InfoDB.getLstChargings(partnerId); 
                resLogin.usrEntity = user;

                resLogin.expr = user.experience;
                resLogin.isActive = user.isActive;
                resLogin.password = user.mPassword;

                if (user.cellPhone == null || "".equals(user.cellPhone)) {
                    resLogin.isPhoneUpdate = true;
                }

                // check event bonus
//                try {
//	                CacheEventInfo cache = new CacheEventInfo();
//	                if (cache.getEventBonus() != null) {
//	                	resLogin.isBonus = true;
//	                }
//                } catch (Exception ex) {
//                	mLog.debug(" Khong lay duoc event bonus " + ex.getMessage());
//                }
                aSession.setUserName(user.mUsername);
                aSession.setLoggedIn(true);
                aSession.setUserEntity(user);

                CommonQueue queue = new CommonQueue();

                QueueEntity entity = new QueueEntity(aSession, resLogin);
                resLogin.deviceUId = rqLogin.deviceUId;
                queue.insertQueue(entity);

            }

            rtn = PROCESS_OK;
        } catch (LoginException ex) {
            resLogin = null;
            aSession.setLoggedIn(false);
            aSession.setCommit(false);

            try {
                userDb.logout(aSession.getUID(), "");
            } catch (SQLException ex1) {
                mLog.error(ex.getMessage(), ex1);
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

}
