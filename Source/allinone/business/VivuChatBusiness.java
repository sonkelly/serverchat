/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.protocol.messages.VivuChatRequest;
import allinone.protocol.messages.VivuChatResponse;

/**
 *
 * @author Vostro 3450
 */
public class VivuChatBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(VivuChatBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        MessageFactory msgFactory = aSession.getMessageFactory();
        VivuChatResponse resMove = (VivuChatResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        mLog.debug("[Vivu Chat]: Catch");
        try {
            VivuChatRequest req = (VivuChatRequest) aReqMsg;
            int type = req.type;
            resMove.setSuccess(req.mMessage, aSession.getUID());
            aSession.broadcast(resMove, type);
        } catch (Throwable t) {
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
            resMove.setFailure("Can not update status!");
            aResPkg.addMessage(resMove);
        }
        return 1;
    }
}
