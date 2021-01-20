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
import allinone.protocol.messages.VivuTradingRequest;
import allinone.protocol.messages.VivuTradingResponse;

/**
 *
 * @author Vostro 3450
 */
public class VivuTradingBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(VivuChatBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        MessageFactory msgFactory = aSession.getMessageFactory();
        VivuTradingResponse resMove = (VivuTradingResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        mLog.debug("[Vivu Trade]: Catch");
        try {
            VivuTradingRequest req = (VivuTradingRequest) aReqMsg;
            int item = req.item;
            boolean isBuy = req.isBuy;
//            if(isBuy){
//                aSession.getUser().addItem(item);
//            }else {
//                aSession.getUser().removeItem(item);
//            }
            // @TODO: update DB money
            resMove.setSuccess();
           aResPkg.addMessage(resMove);
        } catch (Throwable t) {
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
            resMove.setFailure("Try again!");
            aResPkg.addMessage(resMove);
        }
        return 1;
    }
}
