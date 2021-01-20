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

import allinone.data.MessagesID;
import allinone.data.Phone;
import allinone.data.QueueEntity;
import allinone.data.ResponseCode;
import allinone.data.SimpleTable;
import allinone.data.UserEntity;
import allinone.data.ZoneID;
import allinone.databaseDriven.GiftMoneyAuto;

import allinone.databaseDriven.UserDB;
import allinone.databaseDriven.UserInfoDB;
import allinone.databaseDriven.UsersLoginDayDB;

import allinone.protocol.messages.CancelRequest;
import allinone.protocol.messages.LoginRequest;
import allinone.protocol.messages.LoginResponse;
import allinone.queue.data.CommonQueue;
import allinone.server.Server;

import vn.game.common.sendCommon;
import vn.game.db.DBException;

public class LoginBusiness extends AbstractBusiness {

    private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(LoginBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        int rtn = PROCESS_FAILURE;
        // mLog.debug("[LOGIN]: "+aSession.getUserName());

        MessageFactory msgFactory = aSession.getMessageFactory();

        LoginResponse resLogin = (LoginResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        LoginRequest rqLogin = (LoginRequest) aReqMsg;
        String device = rqLogin.device;
        String mobileVersion = rqLogin.mobileVersion;
        String deviceID = rqLogin.deviceId;
        UserDB userDb = new UserDB();
        //SessionDB sdb = new SessionDB();
        //edit by zepp add user admin
//        if (rqLogin.mUsername.equalsIgnoreCase("authenconnetviet")) {
//            aSession.setUserManager(rqLogin);
//            mLog.debug("mUsername authenconnetviet");

        //} else {
        try {
            UserEntity user = null;

            //aSession.setByteProtocol(rqLogin.protocol);
            aSession.setByteProtocol(AIOConstants.PROTOCOL_REFACTOR);
            resLogin.session = aSession;
            resLogin.numberOnline = Server.numberOnline;

            aSession.setMobile(rqLogin.mobileVersion);
            aSession.setMobileDevice(true);
            aSession.setMXHDevice(rqLogin.isMxh);
            resLogin.isMxh = rqLogin.isMxh;
            aSession.setScreenSize(rqLogin.screen);
            aSession.setTopplayVersion(10);

            resLogin.isMobile = true;
            if (rqLogin.device != null) {
                if (rqLogin.device.toUpperCase().contains("ANDROID")) {
                    //mLog.debug("android device");
                    aSession.setDeviceType(AIOConstants.ANDROID_DEVICE);
                } else if (rqLogin.device.toUpperCase().contains("IPHONE")) {
                    //mLog.debug("iphone device");
                    aSession.setDeviceType(AIOConstants.IPHONE_DEVICE);
                } else if (rqLogin.device.toUpperCase().contains("WEB")) {
                    //mLog.debug("web device");
                    aSession.setDeviceType(AIOConstants.WEB_DEVICE);
                    aSession.setMobileDevice(false);
                    resLogin.isMobile = false;
                } else {
                    aSession.setDeviceType(AIOConstants.MOBILE_DEVICE);
                }
                device += rqLogin.device;
            }
            boolean errorLogin = false;
            String msgLogin = "";
            if (rqLogin.mUsername.length() <= 5 || rqLogin.mUsername.length() >= 30) {
                errorLogin = true;
                msgLogin = "Tên tài khoản tối thiểu 6 ký tự";
            } else if (rqLogin.mPassword.length() <= 5) {
                errorLogin = true;
                msgLogin = "Mật khẩu không đúng";
            }
            if (errorLogin) {
                throw new Exception(msgLogin);

            } else {
                // validate login
                try {
                    user = userDb.login(rqLogin.mUsername, rqLogin.mPassword, rqLogin.device, device, rqLogin.screen, aSession.getOnlyIP(), rqLogin.partnerId, resLogin.isMxh, aSession.getRealMoney());
                    if (user != null) {
                        UserInfoDB userinfoObj = new UserInfoDB();
                        user.usrInfoEntity = userinfoObj.getUserInfo(user.mUid);
                        //mLog.debug("user.usrInfoEntity:"+user.usrInfoEntity.countryId);
                    }
                } catch (SQLException ex) {
                    mLog.error("Login Loi database " + ex.getMessage());
                    throw new Exception("Hệ thống tạm thời bảo trì, vui lòng thử lại sau!");
                }
            }
            if (user == null) // non-existed user
            {

                String upperUserName = rqLogin.mUsername.toUpperCase();
                if (aSession.getRetryLogin(upperUserName) > AIOConstants.MAX_RETRY_LOGIN) {
                    throw new LoginException("Brute password");
                }
                aSession.setRetryLogin(upperUserName);

                mLog.debug("***Khong the dang nhap voi user " + rqLogin.mUsername + " password: " + rqLogin.mPassword);
                throw new Exception("Thông tin đăng nhập hoặc mật khẩu chưa chính xác!");

            } else {

                if (user.isLock == 1) {
                    throw new Exception("Tài khoản của bạn đã bị khóa!");
                } else if (user.utype == 7 || user.utype == 8) {
                    throw new Exception("Tài khoản đại lý không đăng nhập được!");
                } else if (user.utype == 0 && Server.isBaoTri) {
                    throw new Exception("Hệ thống tạm thời bảo trì, vui lòng thử lại sau!");
                }
                sendCommon.sendConfigGame(aSession, msgFactory);//config_game
                user.refreshStatus();
                //update time 
                if (user.newDateLogin == 1) {
                    try {
                        if (user.money < 10000 && user.isActive) {//tang tien hang ngay
//                            long realmoney = userDb.updateUserLastLoginGiftDay(user);
//                            user.money = realmoney;

                        } else {
                            userDb.updateUserLastLogin(user);
                        }
                        //bo tang tien thao ngay, ma thay vao la su kien bao danh theo ngay 
                        if (user.utype == 0) {//chi su dung cho tk thuong
                            UsersLoginDayDB.processLoginDay(user);
                        }

                    } catch (DBException ex) {
                        mLog.debug("updateUserLastLogin lgoin1", ex);
                    } catch (SQLException ex) {
                        mLog.debug("updateUserLastLogin lgoin2", ex);
                    }
                }
                //update time login 
                // kiem tra xem user co dang choi trong ban nao khong
                String alertMessage = "";

                alertMessage = this.checkPlayInMatch(resLogin, user.mUid, aSession, user.viewName);
                //mLog.debug("alertMessage: " + alertMessage);
                user.isLogin = true;

                aSession.setUID(user.mUid);
                aSession.setuType(user.utype);
                long moneyUpdateLevel = 0;

                mLog.debug("Login User ID " + user.mUid);
                //tang tien khi het < 10k
                if (user.utype == 0 && user.money <= AIOConstants.GIFT_SUPPORT_MONEY_MIN && user.isActive) {//su kiện ứng tiền 1 lần khi hết tiền
                    long curMoney = GiftMoneyAuto.processGiftAuto(1, user, "money");
                    if (curMoney > 0) {
                        user.money = curMoney;
                    }

                }

                if (!user.isActive) {
                    Phone phone = userDb.getPhone(user.mUid, user.mUsername);
                    resLogin.activeCode = phone.activeCode;
                }

                //end tang tien
                resLogin.setSuccess(ResponseCode.SUCCESS, user.mUid, user.money, user.avatarID, user.level, user.lastLogin, "", user.playsNumber, moneyUpdateLevel, user.viewName, user.realmoney);
                resLogin.expr = user.experience;
                resLogin.level = user.level;
                resLogin.isActive = user.isActive;
                resLogin.cellPhone = user.cellPhone;
                resLogin.newDateLogin = user.newDateLogin;
                resLogin.usrInfoEntity = user.usrInfoEntity;

                if ("".equals(user.cellPhone) || user.cellPhone == null) {
                    resLogin.isPhoneUpdate = true;
                }

                //Login Event like GoldenHour
//                try {
//                    String resLEvent = userDb.loginEvent(user.mUid, rqLogin.mobileVersion);
//                    mLog.debug("LoginEvent: " + resLEvent);
//                    if (resLEvent.compareTo("-1") == 0) {
//                        throw new Exception("Tài khoản không tồn tại!");
//                    } else {
//                        String[] ev = resLEvent.split("#");
//                        if (ev.length == 2) {
//                            user.money = Long.parseLong(ev[0]);
//                            addAno(aSession, ev[1], msgFactory);
//                        } else if (ev.length == 1) {
//                            user.money = Long.parseLong(ev[0]);
//                        } else {
//                            throw new Exception("Tài khoản không tồn tại!");
//                        }
//                    }
//                } catch (Throwable e) {
//                    e.printStackTrace();
//                    throw new Exception("Tài khoản không tồn tại!");
//                }
                boolean checkNewMail = false;
                String mailContent = "";
                String mailTitle = "";
                boolean checkNewEvent = false;

                resLogin.chkEmail = checkNewMail;
                resLogin.chkEvent = checkNewEvent;

                if (!"".equals(alertMessage)) {
                    resLogin.alertEmailContent = alertMessage;
                    resLogin.alertEmailTitle = "Cảnh báo";
                } else {
                    resLogin.alertEmailContent = mailContent;
                    resLogin.alertEmailTitle = mailTitle;
                }

                aSession.setUserName(user.mUsername);
                aSession.setLoggedIn(true);
                aSession.setUserEntity(user);
                //set money type
                if (user.realmoney <= 0) {
                    aSession.setRealMoney(AIOConstants.PRIFIX_REALMONEY);
                }
                // for process ITUNES public
//                if("9603".equals(rqLogin.refCode)) {
//                	resLogin.expr = 9603;                	
//                }
                // add to login queue
                //mLog.debug(" add response to quee");
                CommonQueue queue = new CommonQueue();
                QueueEntity entity = new QueueEntity(aSession, resLogin);
                resLogin.deviceId = rqLogin.deviceId;
                //resLogin.mobileVersion = rqLogin.device + rqLogin.mobileVersion;
                //end send zone config
                queue.insertQueue(entity);
                //add by zep
                //sdb.addSessionDB(user.mUid, aSession.getID(), aSession.getUserName(), aSession.getOnlyIP(), "");//tam thoi bo

                //sendCommon.sendZoneConfig(aSession, msgFactory);
                sendCommon.sendCurrentSessionMoney(aSession, msgFactory);
                sendCommon.sendCountEmail(aSession, msgFactory);
                //sendCommon.sendMsgSystem(aSession, msgFactory);//tạm bỏ chuyển sang client call, vì cơ chế sys có thể phải slow
                sendCommon.sendNumberOnline(aSession, Server.numberOnline + 1);

            }

            rtn = PROCESS_OK;

        } catch (LoginException ex) {
            resLogin = null;
            aSession.setLoggedIn(false);
            aSession.setCommit(false);
//            try {
//                userDb.logout(aSession.getUID(), "");
//            } catch (SQLException ex1) {
//                mLog.error(ex.getMessage(), ex1);
//            }
//            try {
//                sdb.updateSessionDB(aSession.getUID(), "", "", "", "");
//            } catch (Exception e) {
//
//            }
            rtn = PROCESS_OK;
            mLog.warn(ex.getMessage());
        } catch (BusinessException ex) {
            resLogin.setFailure(ResponseCode.FAILURE, ex.getMessage());
            aSession.setLoggedIn(false);
            aSession.setCommit(false);
//            try {
//                userDb.logout(aSession.getUID(), "");
//            } catch (SQLException ex1) {
//                mLog.error(ex.getMessage(), ex1);
//            }
//            try {
//                sdb.updateSessionDB(aSession.getUID(), "", "", "", "");
//            } catch (Exception e) {
//
//            }
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
        //}
        return rtn;
    }

    public String checkPlayInMatch(LoginResponse resLogin, long uid, ISession aSession, String viewname) {

        int currentZone = 0;
        long currentMatchId = 0;
        String alertMessage = "";
        int tableIndex = 0;
        String zoneName = "";
        boolean isPlaying = false;

        try {

            //SessionManager manager = aSession.getManager();
            // Kiem tra xem co dang ton tai trong ban choi game nao khong
            SimpleTable table = SimpleTable.findUserInTable(uid);

            if (table != null) {
                currentMatchId = table.getMatchID();
                currentZone = table.getNotNullSession().getCurrentZone();
                zoneName = ZoneID.getZoneName(currentZone);
                isPlaying = table.isPlayerPlaying(uid);
                tableIndex = table.getTableIndex();
            }

//            mLog.debug(" In Match " + currentMatchId + " isplaying " + isPlaying);
//
//            mLog.debug(" New session " + aSession.getID() + " " + aSession.getUID());
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
            resLogin.viewname = viewname;

        } catch (Exception ex) {
            mLog.equals(" Login check table error " + ex.getMessage());
        }

        return alertMessage;
    }

    public void checkSession(long uid, ISession aSession) {

    }

}
