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
import allinone.protocol.messages.GetWapGameResponse;


/**
 *
 * @author mcb
 */
public class GetWapGameBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetWapGameBusiness.class);
    
    private static String WAP_LINK = "http://m.landsoft.vn?p=";
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
        GetWapGameResponse resWapGame = (GetWapGameResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        //resWapGame.setSuccess(WAP_LINK);
        resWapGame.setFailure("Tính năng này sẽ sớm ra mắt");
        aResPkg.addMessage(resWapGame);
        return 1;
    }
}
