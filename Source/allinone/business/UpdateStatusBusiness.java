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
import allinone.protocol.messages.UpdateStatusRequest;
import allinone.protocol.messages.UpdateStatusResponse;

/**
 *
 * @author Vostro 3450
 */
public class UpdateStatusBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(UpdateStatusBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        MessageFactory msgFactory = aSession.getMessageFactory();
            UpdateStatusResponse resMove = (UpdateStatusResponse) msgFactory.getResponseMessage(aReqMsg.getID());
            mLog.debug("[Update Status]: Catch");
        try {
            UpdateStatusRequest req = (UpdateStatusRequest) aReqMsg;
            resMove.setSuccess(req.buddy_uid, aSession.getUID(), req.status);
            aSession.write(resMove);
            aSession.broadcast(resMove,0);
        } catch (Throwable t) {
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
            resMove.setFailure("Can not update status!");
            aResPkg.addMessage(resMove);
        }
        return 1;
    }
}
