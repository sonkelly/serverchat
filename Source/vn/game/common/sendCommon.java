/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.game.common;

import allinone.data.AIOConstants;
import allinone.data.AlertUserEntity;
import allinone.data.CardTxnEntity;
import allinone.data.HTTPPoster;
import allinone.data.MessagesID;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.databaseDriven.CardTxnDB;
import allinone.databaseDriven.InfoDB;
import allinone.databaseDriven.MailDB;
import allinone.databaseDriven.UserDB;
import allinone.databaseDriven.ZoneDB;
import allinone.protocol.messages.ChangeMoneyTypeResponse;
import allinone.protocol.messages.ConfigGameResponse;
import allinone.protocol.messages.CountEmailResponse;
import allinone.protocol.messages.SendAdvResponse;
import allinone.protocol.messages.UpdateCashResponse;
import allinone.protocol.messages.ZoneConfigResponse;
import allinone.protocol.messages.NumberOnlineResponse;
import allinone.server.Server;
import com.google.gson.Gson;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.json.JSONObject;
import org.slf4j.Logger;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import vn.game.session.SessionManager;

/**
 *
 * @author Zeple
 */
public class sendCommon {

    private static boolean isInProgress = false;
    private static boolean isInProgressRongVang = false;
    private static boolean isInNumberOnlieSend = false;
    private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(sendCommon.class);

    public static void sendZoneConfig(ISession aSession, MessageFactory msgFactory) {
        //send zone config

        ZoneDB zoneOBJ = new ZoneDB();
        String zonestr = zoneOBJ.getStrZone();

        ZoneConfigResponse betResRed = (ZoneConfigResponse) msgFactory.getResponseMessage(MessagesID.ZONE_CONFGIG);
        betResRed.setSuccess(1, zonestr);
        betResRed.setSession(aSession);
        try {
            aSession.write(betResRed);
        } catch (ServerException ex) {
            mLog.error("sendMsgRefund ServerException" + ex.getMessage(), ex);
        }
    }

    public static void sendConfigGame(ISession aSession, MessageFactory msgFactory) {
        //send zone config

        getConfigGame cfgame = new getConfigGame();
        Map<String, String> allconfigGame = cfgame.getAllConfig();
        //String str_resulte = "{}";
        String str_resulte = new Gson().toJson(allconfigGame);

        ConfigGameResponse resConfigGame = (ConfigGameResponse) msgFactory.getResponseMessage(MessagesID.CONFIG_GAME);
        resConfigGame.setSuccess(1, str_resulte);
        resConfigGame.setSession(aSession);
        try {
            aSession.write(resConfigGame);
        } catch (ServerException ex) {
            mLog.error("sendMsgRefund ServerException" + ex.getMessage(), ex);
        }
    }

    public static void sendCountEmail(ISession aSession, MessageFactory msgFactory) {
        int countmail = MailDB.getCountMail(aSession.getUID());
        CountEmailResponse mailResCount = (CountEmailResponse) msgFactory.getResponseMessage(MessagesID.COUNT_EMAIL);
        String smsKH = "VMG CM nap1 KH" + aSession.getUID();
        String smsKH_VINA = "VMG CM nap1 KH" + aSession.getUID();
        String smsKH_MOBI = "VMG CM nap1 KH" + aSession.getUID();
        mailResCount.setSuccess(countmail, smsKH, smsKH_VINA, smsKH_MOBI);
        mailResCount.setSession(aSession);
        try {
            aSession.write(mailResCount);
        } catch (ServerException ex) {
            mLog.error("sendMsgRefund ServerException" + ex.getMessage(), ex);
        }
    }

    public static void checkWaitRecharge(ISession session) {
        try {

            UserDB userDb = new UserDB();
            UserEntity userObj = userDb.getUserInfo(session.getUID(), session.getRealMoney());
            if (userObj != null) {
//                if(userObj.money != session.getUserEntity().money){
                try {
                    //get card txn
                    CardTxnEntity cardObj = CardTxnDB.getLog(session.getUID(), session.getWaitRecharge());
                    if (cardObj != null) {
                        if (cardObj.statusId != 1) {
                            //mLog.debug("ok da goi xong");
                            session.setWaitRecharge(0);
                            MessageFactory msgFactory = session.getMessageFactory();
                            sendCommon.sendUpdateMoney(session, msgFactory);
                        }

                    } else {
                        session.setWaitRecharge(0);
                    }

                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(sendCommon.class.getName()).log(Level.SEVERE, null, ex);
                }
//                   
//                    
//                }
            }
        } catch (SQLException ex) {
            //java.util.logging.Logger.getLogger(CommonMonitorJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //send update money
    public static void sendUpdateMoney(ISession aSession, MessageFactory msgFactory) {

        UpdateCashResponse moneyResUpdate = (UpdateCashResponse) msgFactory.getResponseMessage(MessagesID.UPDATEMONEY);

        moneyResUpdate.setSuccessUpdateMoney(1, aSession.getUID(), aSession.getUserEntity().viewName, aSession.getUserEntity().money,aSession.getUserEntity().realmoney, aSession.getUserEntity().isActive);
        moneyResUpdate.setSession(aSession);
        try {
            aSession.write(moneyResUpdate);
        } catch (ServerException ex) {
            mLog.error("sendMsgRefund ServerException" + ex.getMessage(), ex);
        }
    }

    //send events
    public static void sendMsgSystem(ISession aSession, MessageFactory msgFactory) {
//        String msg = "";
//        int type = -1;
//        String signStr = aSession.getUID() + "|" + aSession.getUserName() + "|" + "|" + "23aasd22#4@1490kdj34&^45u234";
//        //String sign = MD5.toMD5(signStr);
//        String sign = MD5.toMD5(signStr);
//
//        StringBuffer url = new StringBuffer();
//        url.append("").append("&uid=").append(aSession.getUID());
//        try {
//            HTTPPoster p = new HTTPPoster();
//            p.setParameCall(HTTPPoster.API_SYS_MSG);
//            String res = p.callPost(url.toString());
//            mLog.debug("res:" + res);
//            if (res != null) {
//
//                JSONObject json = new JSONObject(res);
//
//                if (json != null) {
//                    int error = json.getInt("error");
//                    if (error == 0) {
//                        type = json.getInt("type");
//                        msg = json.getString("msg");
//                    }
//                }
//            }
//        } catch (Exception e) {
//
//        }
//        if (!msg.equals("")) {
//            mLog.debug("add send adv common");
////            CommonQueue queue = new CommonQueue();
////
////            //it 's new device send to client
////            SendAdvResponse advRes = (SendAdvResponse) msgFactory.getResponseMessage(MessagesID.MSG_SYSTEM);
////            advRes.session = aSession;
////            advRes.type = type;
////            advRes.setSuccess(msg, type);
////            QueueEntity entity = new QueueEntity(aSession, advRes);
////            queue.insertQueue(entity);
////            mLog.debug("end send adv common");
//            StringBuilder sb = new StringBuilder();
//            sb.append(type).append(AIOConstants.SEPERATOR_BYTE_1);
//
//            sb.append(msg);
//
//            SendAdvResponse moneyResUpdate = (SendAdvResponse) msgFactory.getResponseMessage(MessagesID.MSG_SYSTEM);
//
//            moneyResUpdate.setSuccess(sb.toString(), type);
//            moneyResUpdate.setSession(aSession);
//            try {
//                aSession.write(moneyResUpdate);
//
//            } catch (ServerException ex) {
//                mLog.error("sendMsgRefund ServerException" + ex.getMessage(), ex);
//            }
//
//        }
//        sendCommon commonObj = new sendCommon();
//        commonObj.alertUser(aSession);
        //commonObj.alertBaoTri();
    }

    public static void ApiGiftMoneyAuto(ISession aSession, MessageFactory msgFactory) {
        String msg = "";
        int type = -1;
        String signStr = aSession.getUID() + "|" + aSession.getUserName() + "|" + "|" + "23aasd22#4@1490kdj34&^45u234";
        String sign = MD5.toMD5(signStr);

        StringBuffer url = new StringBuffer();
        url.append("").append("&uid=").append(aSession.getUID()).append("&sign=").append(sign);
        try {
            HTTPPoster p = new HTTPPoster();
            p.setParameCall(HTTPPoster.API_GIFT_MONEY_AUTO);
            String res = p.callPost(url.toString());
            mLog.debug("res:" + res);
            if (res != null) {

                JSONObject json = new JSONObject(res);

                if (json != null) {
                    int error = json.getInt("error");
                    if (error == 0) {
                        type = json.getInt("type");
                        msg = json.getString("msg");
                    }
                }
            }
        } catch (Exception e) {

        }

    }

    private void alertUser(ISession aSession) {
        String strBaitri = this.alertBaoTri();
        if (strBaitri.equals("")) {
            try {
                UserDB db = new UserDB();
                List<AlertUserEntity> lstAlerts = db.getAlertUser(aSession.getUID());

                int size = lstAlerts.size();
                mLog.debug("alertUser:" + size);
                if (size > 0) {

                    Date dtNow = new Date();

                    MessageFactory msgFactory = aSession.getMessageFactory();

                    for (int i = 0; i < size; i++) {
                        AlertUserEntity alertEntity = lstAlerts.get(i);

                        SendAdvResponse advRes = (SendAdvResponse) msgFactory.getResponseMessage(MessagesID.MSG_SYSTEM);

                        StringBuilder sb = new StringBuilder();

                        sb.append(Integer.toString(1)).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(alertEntity.getContent());
                        advRes.setSuccess(sb.toString(), 1);
                        advRes.setSession(aSession);
                        try {
                            aSession.write(advRes);
                        } catch (ServerException ex) {
                            mLog.error("eror send alertUser" + ex.getMessage(), ex);
                        }
                    }
                }
            } catch (SQLException ex) {
                mLog.error(ex.getMessage(), ex);
            }
        }
    }

    private String alertBaoTri() {
        String str = "";
        try {
            InfoDB db = new InfoDB();
            AlertUserEntity alertObj = db.getMaintenance();

            mLog.debug(alertObj.getContent());
            str = alertObj.getContent();
            if (!str.equals("")) {
                SessionManager sm = Server.getWorker().getmSessionMgr();
                Enumeration values;

                values = null;
                values = sm.getmSessions().elements();
                mLog.debug("sm.getmSessions().values().size():" + sm.getmSessions().values().size());
                if ((values != null) && (sm.getmSessions().values().size() > 0)) {

                    for (ISession session : sm.getmSessions().values()) {
                        mLog.debug(str + " for");
                        SendAdvResponse advRes = (SendAdvResponse) session.getMessageFactory().getResponseMessage(MessagesID.MSG_SYSTEM);
                        StringBuilder sb = new StringBuilder();

                        sb.append(Integer.toString(1)).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(str);
                        advRes.setSuccess(sb.toString(), 1);
                        advRes.setSession(session);
                        mLog.debug("user msg" + session.getUserName());
                        try {

                            try {
                                session.write(advRes);
                                if (alertObj.disconnect == 1) {
                                    session.cancelTable();
                                    session.close();
                                }
                            } catch (ServerException ex) {
                                mLog.error("check ServerException", ex);
                            }

                        } catch (Exception ex) {
                            mLog.error("check connection", ex);
                        }

                    }
                }
            }
        } catch (Exception e) {

        }
        return str;
    }

    //so nguoi online
    public static void sendNumberOnline(ISession aSession, int numberOnline) {

        NumberOnlineResponse resNumber = (NumberOnlineResponse) aSession.getMessageFactory().getResponseMessage(MessagesID.NUMBER_ONLINE);

        resNumber.setSuccess(ResponseCode.SUCCESS, numberOnline);
        resNumber.setSession(aSession);
        try {
            aSession.write(resNumber);
        } catch (ServerException ex) {
            mLog.error("resNumber ServerException" + ex.getMessage(), ex);
        }

    }
    public static void sendNumberOnlineAll(int numberOnline) {

        if (!isInNumberOnlieSend) {
            isInNumberOnlieSend = true;
            try {
                
                SessionManager sm = Server.getWorker().getmSessionMgr();
                Enumeration values;

                values = null;
                values = sm.getmSessions().elements();
                //mLog.debug("numberOnline send:" + numberOnline);
                if ((values != null) && (sm.getmSessions().values().size() > 0)) {

                    for (ISession session : sm.getmSessions().values()) {
                        if (session != null && !session.realDead() && !session.isExpired() && session.isLoggedIn() && session.getuType() == 0) {
                            NumberOnlineResponse resNumber = (NumberOnlineResponse) session.getMessageFactory().getResponseMessage(MessagesID.NUMBER_ONLINE);

                            resNumber.setSuccess(ResponseCode.SUCCESS, numberOnline);
                            resNumber.setSession(session);
                            try {
                                session.write(resNumber);
                            } catch (ServerException ex) {
                                mLog.error("resNumber ServerException" + ex.getMessage(), ex);
                            }
                            
                        }

                    }
                }
            } catch (Exception ex) {
                mLog.error("sendMsgRefund ServerException" + ex.getMessage(), ex);
            }
        }
        isInNumberOnlieSend = false;
        

    }
    //send update money
    public static void sendCurrentSessionMoney(ISession aSession, MessageFactory msgFactory) {

        ChangeMoneyTypeResponse moneyResUpdate = (ChangeMoneyTypeResponse) msgFactory.getResponseMessage(MessagesID.CHANGE_MONEY_TYPE);
        int moneyType = 0;
        if (aSession.getRealMoney().equals(AIOConstants.PRIFIX_REALMONEY)) {
            moneyType = 1;
        }
        moneyResUpdate.setSuccess(ResponseCode.SUCCESS, moneyType);
        try {
            aSession.write(moneyResUpdate);
        } catch (ServerException ex) {
            mLog.error("sendMsgRefund ServerException" + ex.getMessage(), ex);
        }
    }

}
