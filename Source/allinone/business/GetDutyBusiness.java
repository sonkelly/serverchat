/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.databaseDriven.DutyDB;
import allinone.protocol.messages.GetDutyResponse;


/**
 *
 * @author mcb
 */
public class GetDutyBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetDutyBusiness.class);
    
//    private static String WAP_LINK = "http://m.landsoft.vn?p=";
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
//        mLog.warn("Reload cache");
//        DBCache.reload();
//           try{
//                    Server.getWorker().getTourMgr().reload();
//            } catch (Throwable e) {
//                    mLog.error(e.getMessage());
//                    }
//        BotRequest rqBot = (BotRequest) aReqMsg;
//        mLog.debug("bot session");
//        CacheUserInfo cache = new CacheUserInfo();
//        UserEntity entity = cache.getUserInfo(aSession.getUID());
        
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetDutyResponse resWapGame = (GetDutyResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        DutyDB db = new DutyDB();
        resWapGame.setSuccess(db.getListDuty());
        aResPkg.addMessage(resWapGame);
        return 1;
    }
}
