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
import allinone.data.ResponseCode;
import allinone.protocol.messages.RecommendResponse;


/**
 *
 * @author mcb
 */
public class RecommendBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(RecommendBusiness.class);
    

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {

        MessageFactory msgFactory = aSession.getMessageFactory();
        RecommendResponse resWapGame = (RecommendResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        
        resWapGame.setSuccess(ResponseCode.SUCCESS);
        aResPkg.addMessage(resWapGame);
        return 1;
    }
}
