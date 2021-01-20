/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.Room;
import vn.game.session.ISession;
import allinone.data.ZoneID;
import allinone.protocol.messages.CancelChallengeRequest;
import allinone.protocol.messages.CancelChallengeResponse;

/**
 *
 * @author mcb
 */
public class CancelChallengeBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(CancelChallengeBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[BET] : Catch  ; " + aSession.getUserName());
        MessageFactory msgFactory = aSession.getMessageFactory();
        CancelChallengeResponse resCancel = (CancelChallengeResponse) msgFactory
                .getResponseMessage(aReqMsg.getID());
        // boolean isFinish = false;
        try {
            CancelChallengeRequest rqCancel = (CancelChallengeRequest) aReqMsg;
            Room room = aSession.getRoom();

            // Thread.sleep(10000);
            if (room == null) {
                throw new BusinessException("Bạn cần tham gia vào một trận trước khi chơi.");
            }

           

        }catch (BusinessException ex) {
            mLog.error("Process message " + aReqMsg.getID() + " error." + ex.getMessage());
            resCancel.setFailure(ex.getMessage());

            mLog.error("Invalid " + ex.getMessage());

            try {
                aSession.write(resCancel);
            } catch (ServerException se) {

            }

        } catch (Throwable t) {
            //resBet.setFailure(ResponseCode.FAILURE, t.getMessage());
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
            try {
                aSession.write(resCancel);
            } catch (ServerException ex) {
                // java.util.logging.Logger.getLogger(TurnBusiness.class.getName()).log(Level.SEVERE,
                // null, ex);
            }

        }

        return 1;
    }
}
