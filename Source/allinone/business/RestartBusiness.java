package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.Room;
import vn.game.room.Zone;
import vn.game.session.ISession;
import allinone.data.MessagesID;
import allinone.data.ResponseCode;
import allinone.protocol.messages.CancelResponse;
import allinone.protocol.messages.RestartRequest;
import allinone.protocol.messages.RestartResponse;

public class RestartBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(RestartBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[RESTART] : Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        RestartResponse resMatchReturn = (RestartResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        CancelResponse resCancelMatch = (CancelResponse) msgFactory.getResponseMessage(MessagesID.MATCH_CANCEL);
        try {
            RestartRequest rqReturn = (RestartRequest) aReqMsg;
            Zone bacayZone = aSession.findZone(aSession.getCurrentZone());
            Room room = bacayZone.findRoom(rqReturn.mMatchId);
            resMatchReturn.setZoneID(aSession.getCurrentZone());
            if (room != null) {
                mLog.debug("[RESTART] Current room = " + room.getName());

                switch (aSession.getCurrentZone()) {
                    
                    default:
                        break;
                }


            } else {
                resMatchReturn.setFailure(ResponseCode.FAILURE,
                        "Bạn cần tham gia vào một trận trước khi chơi.", false);
            }

        } catch (Throwable t) {
            resMatchReturn.setFailure(ResponseCode.FAILURE, "Bị lỗi " + t.toString(), false);
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally {
            if ((resMatchReturn != null)) {
                //aResPkg.addMessage(resMatchReturn);
            }
        }
        return 1;
    }
}
