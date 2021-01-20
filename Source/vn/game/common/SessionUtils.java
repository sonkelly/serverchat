/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.game.common;

import allinone.business.LoginBusiness;
import allinone.data.SessionEntity;
import allinone.databaseDriven.SessionDB;
import java.util.logging.Level;
import java.util.logging.Logger;
import vn.game.protocol.messages.ExpiredSessionResponse;

import vn.game.session.ISession;

/**
 *
 * @author Zeple
 */
public class SessionUtils {

    private static final org.slf4j.Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(SessionUtils.class);

    public static boolean checkSessionDB(ISession aSession) {
        
//        mLog.error("checkSessionDB ");
//        try {
//            SessionDB sdbObj = new SessionDB();
//            SessionEntity sdb = sdbObj.getSession(aSession.getUID());
//            if (sdb != null) {
//                if (!aSession.getID().equals(sdb.sid)) {
//                    mLog.error("checkSessionDB Vui lòng đăng nhập lại");
//                    try {
//                        sdbObj.updateSessionDB(aSession.getUID(), aSession.getID(), aSession.getUserName(), aSession.getOnlyIP(), "");
//                        ExpiredSessionResponse expiredSession = (ExpiredSessionResponse) aSession.getMessageFactory().getResponseMessage(9999);
//                        expiredSession.mErrorMsg = "Vui lòng đăng nhập lại.";
//                        aSession.write(expiredSession);
//                        aSession.setLoggedIn(false);
//                        aSession.setUIDNull();
//                        
//                        return false;
//                    } catch (ServerException e) {
//                        mLog.error("checkSessionDB error: " + e.getCause());
//                    }
//                }
//            } else {
//                sdbObj.addSessionDB(aSession.getUID(), aSession.getID(), aSession.getUserName(), aSession.getOnlyIP(), "");
//            }
//        } catch (Exception ex) {
//            mLog.error("checkSessionDB error");
//        }
        return true;
    }

}
