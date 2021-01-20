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
import vn.game.session.ISession;
import allinone.protocol.messages.MovingRequest;

/**
 *
 * @author Vostro 3450
 */
public class MovingBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(ChatBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        //MessageFactory msgFactory = aSession.getMessageFactory();
        //MovingResponse resMove = (MovingResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        mLog.debug("[Move]: Catch");
        try {
            MovingRequest rqMove = (MovingRequest) aReqMsg;
            int x = rqMove.x;
            int y = rqMove.y;
            long uid = aSession.getUID();
            aSession.getUser().setPosition(x, y);//Update Position
            aSession.getGroup().insertBuffer(uid, x, y);
            //resMove.setSuccess(x, y, uid, "", "", true);
            //aSession.broadcast(resMove,0);
        } catch (Throwable t) {
            //resMove.setFailure("Không di chuyển được này");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
            //aResPkg.addMessage(resMove);
        }
        return 1;
    }
}
