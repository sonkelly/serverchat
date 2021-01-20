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
import allinone.data.OpenUserEntity;
import allinone.data.Phone;
import allinone.data.ZoneID;
import allinone.databaseDriven.GiftMoneyAuto;
import allinone.databaseDriven.UserDB;
import allinone.databaseDriven.OpenUserDB;
import allinone.databaseDriven.SessionDB;
import allinone.databaseDriven.UserInfoDB;
import allinone.databaseDriven.UsersLoginDayDB;
import allinone.protocol.messages.CancelRequest;
import allinone.protocol.messages.LogoutRequest;
import allinone.protocol.messages.PlaySocialLoginRequest;
import allinone.protocol.messages.PlaySocialLoginResponse;
import allinone.queue.data.CommonQueue;
import allinone.server.Server;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import vn.game.common.MD5;
import vn.game.common.sendCommon;
import vn.game.db.DBException;

public class PlaySocialLoginBusiness extends AbstractBusiness {

    private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(PlaySocialLoginBusiness.class);

//    private void addAno(ISession aSession, String ano, MessageFactory msgFactory) {
//        CommonQueue queue = new CommonQueue();
//        SendAdvResponse advRes = (SendAdvResponse) msgFactory.getResponseMessage(MessagesID.SEND_ADV);
//        advRes.session = aSession;
//        StringBuilder sb = new StringBuilder();
//        sb.append(Integer.toString(1)).append(AIOConstants.SEPERATOR_BYTE_1);
//        sb.append(ano);
//        advRes.setSuccess(sb.toString());
//        QueueEntity entity = new QueueEntity(aSession, advRes);
//        queue.insertQueue(entity);
//    }
    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        int rtn = PROCESS_FAILURE;
        mLog.debug("[SOCIAL LOGIN]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        PlaySocialLoginResponse resLogin = (PlaySocialLoginResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        PlaySocialLoginRequest rqLogin = (PlaySocialLoginRequest) aReqMsg;
        sendCommon.sendConfigGame(aSession, msgFactory);//config_game
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
        OpenUserDB openUserDB = new OpenUserDB();
        SessionDB sdb = new SessionDB();
        try {
            UserEntity user = null;
            resLogin.session = aSession;
            aSession.setByteProtocol(AIOConstants.PROTOCOL_REFACTOR);
            resLogin.numberOnline = Server.numberOnline;
            aSession.setMobileDevice(true);

//            if (rqLogin.mobileVersion != null) {
//                if (rqLogin.mobileVersion.toUpperCase().contains("ANDROID")) {
//                    mLog.debug("android device");
//                    aSession.setDeviceType(AIOConstants.ANDROID_DEVICE);
//                } else if (rqLogin.mobileVersion.toUpperCase().contains("IPHONE")) {
//                    mLog.debug("iphone device");
//                    aSession.setDeviceType(AIOConstants.IPHONE_DEVICE);
//                } else {
//                    aSession.setDeviceType(AIOConstants.MOBILE_DEVICE);
//                }
//            }
            aSession.setTopplayVersion(10);
            resLogin.isMobile = true;
//test business
            String token = rqLogin.token;
            if (token.equals("") || token.length() <= 10) {
                throw new Exception("Dữ liệu không đúng!");
            }
//            String facebookBusinessToken = this.getTokenBusiness(token);
//
//            JSONObject jsonToken = (JSONObject) JSONSerializer.toJSON(facebookBusinessToken);
//            String facebookBusiness = this.getIdsBusiness(token, jsonToken.getString("token_for_business"), jsonToken.getString("id"));
//            TraceLog.instance().debugTrace(new Object[]{"facebookBusiness:", facebookBusiness});
//            byte[] facebookBusinessmessage = null;
//            try {
//                facebookBusinessmessage = facebookBusiness.getBytes("UTF-8");
//            } catch (UnsupportedEncodingException ex) {
//
//            }
//            String facebookBusinessEncode = DatatypeConverter.printBase64Binary(facebookBusinessmessage);

//            String facebookData = this.getFBGraph(token);
//            FacebookData usDataFBTemp = null;
//
//            try {
//
//                JSONObject fbData = new JSONObject(facebookData);
//
//                usDataFBTemp = new FacebookData(fbData.getString("id"), fbData.getString("id") + "@gmail.com", fbData.getString("name"));
//            } catch (Exception e) {
//
//            }
//            if (usDataFBTemp == null || usDataFBTemp.id.equals("")) {
//                throw new Exception("Dữ liệu không đúng.!");
//            }
//            mLog.debug(usDataFBTemp.name);
            String open_id = rqLogin.socialId.replaceAll("fb_", "");
            try {
                //user = userDb.socialLogin(rqLogin.socialId, rqLogin.partnerId, rqLogin.refCode, rqLogin.mobileVersion, rqLogin.deviceType, aSession.getOnlyIP());
                //edit by zep

                mLog.debug("Login open_id " + open_id);
                OpenUserEntity usopen = openUserDB.getOpenUser(open_id);
                if (usopen == null) {//chua co thi lay tu facebook
                    String pass = MD5.toMD5("netvietS2017#a@");
                    String ip = aSession.getOnlyIP().replace("/", "");

                    String countryCode = "vn";
                    try {
                        CountryEntity countryObj = new CountryEntity();
                        countryCode = countryObj.getCountryByIP(ip);

                    } catch (Exception e) {
                        mLog.debug("Excpetion " + e.getMessage());
                    }

                    long uidOpen = userDb.registerUserMxh("fb" + open_id, pass, 0, "", 0, false, 0, true, rqLogin.refCode, 1, rqLogin.deviceID, 1, rqLogin.deviceType, ip, aSession.getRealMoney(), countryCode);
                    if (uidOpen > 0) {
                        openUserDB.insertOpenUser(open_id, (int) uidOpen, "fb" + open_id + "@facebook.com", 1, "facebook.com/" + open_id, "");
                        user = userDb.getUserInfoFullMoney(uidOpen);
                        //xy ly created user nodebb
//                        if (Server.isNodeBB.length() > 5) {
//                            NodeBBBusiness.createUserNodeBB(user.mUid, user.mUsername, pass);
//                        }
                        //insert users_info
                        if (user != null) {
                            UserInfoDB uInfoDB = new UserInfoDB();
                            uInfoDB.insert(user.mUid, countryCode);
                            //end insert user_info

                            UserInfoDB userinfoObj = new UserInfoDB();
                            user.usrInfoEntity = userinfoObj.getUserInfo(user.mUid);
                            //mLog.debug("user.usrInfoEntity:"+user.usrInfoEntity.countryId);

                        }
                    }
                } else {
                    mLog.debug("Login usopen.uid:" + usopen.uid);
                    user = userDb.fbLogin(usopen.uid, aSession.getRealMoney());
                    if (user != null) {
                        UserInfoDB userinfoObj = new UserInfoDB();
                        user.usrInfoEntity = userinfoObj.getUserInfo(user.mUid);

                    }
                }
                //end edit by zep
            } catch (SQLException ex) {
                mLog.error("Login Loi database " + ex.getMessage());
                throw new Exception("Hệ thống tạm thời bảo trì, vui lòng thử lại sau!");
            }

            if (user == null) // non-existed user
            {
                String upperUserName = rqLogin.socialId.toUpperCase();
                if (aSession.getRetryLogin(upperUserName) > AIOConstants.MAX_RETRY_LOGIN) {
                    throw new LoginException("Không thể đăng nhập");
                }
                aSession.setRetryLogin(upperUserName);
                mLog.warn("***Khong the dang nhap voi " + rqLogin.socialId);
                throw new Exception("Không thể đăng nhập");

            } else {

                if (user.isLock == 1) {
                    throw new Exception("Tài khoản của bạn đã bị khóa!");
                } else if (user.utype == 7 || user.utype == 8) {
                    throw new Exception("Tài khoản đại lý không đăng nhập được!");
                } else if (Server.isBaoTri) {
                    throw new Exception("Hệ thống tạm thời bảo trì, vui lòng thử lại sau!");
                }
                user.refreshStatus();
                //update time 
                if (user.newDateLogin == 1) {
                    try {

                        if (user.money < 1000) {//tang tien hang ngay
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
                        java.util.logging.Logger.getLogger(LoginBusiness.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        java.util.logging.Logger.getLogger(LoginBusiness.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                mLog.warn("Check is logined" + user.isOnline);

//                ISession temp = aSession.getManager().findSession(user.mUid);         
//                if (temp != null && (temp.getUserName() != null && !temp.getUserName().equals("")) && !temp.realDead() && !temp.isExpired() && temp.isLoggedIn()) {
//                		mLog.warn("[UserName] is logged " + user.mUsername);
//                		
////                        temp.setLastAccessTime(new Date());
////                        throw new Exception("Tài khoản này đã đăng nhập!");
//                
//                
//                } 
                // kiem tra xem user co dang choi trong ban nao khong
                String alertMessage = this.checkPlayInMatch(resLogin, user.mUid, aSession);

                user.isLogin = true;

                // su dung ten that
//                if(user.name != null && !"".equals(user.name)) {
//                	user.mUsername = user.name.trim();
//                }
//tang tien khi het < 10k//chi 1 lan
                if (user.utype == 0 && user.money <= AIOConstants.GIFT_SUPPORT_MONEY_MIN) {
                    long curMoney = GiftMoneyAuto.processGiftAuto(1, user, "money");
                    if (curMoney > 0) {
                        user.money = curMoney;
                    }

                }
                //end tang tien
                //fix fbava
//                if (user.groupId == 1) {//tk fb
//                    user.avatarID = "fb" + open_id;
//                }
                 if (!user.isActive) {
                    Phone phone = userDb.getPhone(user.mUid, user.mUsername);
                    resLogin.activeCode = phone.activeCode;
                }

            
                mLog.debug("user.avatarID:" + user.avatarID);
                aSession.setUID(user.mUid);
                aSession.setuType(user.utype);
                resLogin.setSuccess(ResponseCode.SUCCESS, user.mUid, user.money, user.realmoney, user.lastLogin, user.avatarID, user.viewName, user.level);
                resLogin.expr = user.experience;
                resLogin.level = user.level;
                resLogin.isActive = user.isActive;
                resLogin.password = user.mPassword;
                resLogin.mail = user.mail;
                resLogin.newDateLogin = user.newDateLogin;
                resLogin.usrInfoEntity = user.usrInfoEntity;
                //mLog.debug(" user phone " + user.cellPhone + " phone update " + resLogin.isPhoneUpdate);
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
//set money type
                aSession.setRealMoney(AIOConstants.PRIFIX_REALMONEY);
//                if (user.realmoney <= 0) {
//                    aSession.setRealMoney(AIOConstants.PRIFIX_MONEY);
//                }
                // for process ITUNES public
//                if("9603".equals(rqLogin.refCode)) {
//                	resLogin.expr = 9603;                	
//                }
                CommonQueue queue = new CommonQueue();
                QueueEntity entity = new QueueEntity(aSession, resLogin);
                queue.insertQueue(entity);
                //add by zep

                sendCommon.sendCurrentSessionMoney(aSession, msgFactory);
                sendCommon.sendCountEmail(aSession, msgFactory);

                sendCommon.sendNumberOnline(aSession, Server.numberOnline + 1);
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

    public String checkPlayInMatch(PlaySocialLoginResponse resLogin, long uid, ISession aSession) {

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

    //add by zep
    public String getFBGraph(String tocken) {
        String graph = null;
        try {

            String g = "https://graph.facebook.com/me?fields=id,name,email&access_token=" + tocken;
            URL u = new URL(g);
            URLConnection c = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    c.getInputStream()));
            String inputLine;
            StringBuffer b = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                b.append(inputLine + "\n");
            }
            in.close();
            graph = b.toString();

        } catch (Exception e) {

        }
        return graph;
    }

    public String getTokenBusiness(String tocken) {
        String graph = null;
        try {

            String g = "https://graph.facebook.com/me?fields=token_for_business&access_token=" + tocken;
            //String g = "https://graph.facebook.com/2.8/445828778919328/system_users?access_token="+tocken;
            URL u = new URL(g);
            URLConnection c = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    c.getInputStream()));
            String inputLine;
            StringBuffer b = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                b.append(inputLine + "\n");
            }
            in.close();
            graph = b.toString();

        } catch (Exception e) {

        }
        return graph;
    }

    public String getIdsBusiness(String token, String tockenBusiness, String uidBusiness) {
        String graph = null;
        try {
            StringBuffer g = new StringBuffer();
            //g.append("https://graph.facebook.com/me?fields=ids_for_business&token_for_business=").append(tocken);
            g.append("https://graph.facebook.com/");
            g.append(uidBusiness);
            g.append("?fields=ids_for_business&token_for_business=").append(tockenBusiness);
            g.append("&oauth_token=").append(token);
            g.append("&algorithm=").append("HMAC-SHA256");
            g.append("&user_id=").append(uidBusiness);

            URL u = new URL(g.toString());
            URLConnection c = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    c.getInputStream()));
            String inputLine;
            StringBuffer b = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                b.append(inputLine + "\n");
            }
            in.close();
            graph = b.toString();

        } catch (Exception e) {

        }
        return graph;
    }

}
