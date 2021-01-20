/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.session.ISession;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.BotRequest;

/**
 *
 * @author mcb
 */
public class BotBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(BotBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
//        mLog.warn("Reload cache");
//        DBCache.reload();
//           try{
//                    Server.getWorker().getTourMgr().reload();
//            } catch (Throwable e) {
//                    mLog.error(e.getMessage());
//                    }
        BotRequest rqBot = (BotRequest) aReqMsg;
        mLog.debug("bot session");
        UserDB db = new UserDB();
        if (db.checkBotUser(aSession.getUID())) {
            aSession.setBotType(rqBot.botType);
        }

        return 1;
    }
}
