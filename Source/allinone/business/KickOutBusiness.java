package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.Room;
import vn.game.room.Zone;
import vn.game.session.ISession;
import allinone.data.MessagesID;
import allinone.data.ResponseCode;
import allinone.protocol.messages.KickOutRequest;
import allinone.protocol.messages.KickOutResponse;
import allinone.protocol.messages.OutResponse;

public class KickOutBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(KickOutBusiness.class);
    private static final String NOT_OWNER_PERSON="Bạn không phải là chủ bàn - không có quyền đuổi người khác!";
    private static final String PLAYER_OUT="Người chơi này đã thoát rồi!";
    private static final String PLAYING_TABLE="Bạn không thể đuổi người khác khi bàn đang chơi!";
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) throws ServerException {
        mLog.debug("[KICK OUT] : Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        KickOutResponse resKickOut = (KickOutResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        resKickOut.session = aSession;
        try {
            KickOutRequest rqKickOut = (KickOutRequest) aReqMsg;
            Zone bacayZone = aSession.findZone(aSession.getCurrentZone());
            Room currentRoom = bacayZone.findRoom(rqKickOut.mMatchId);
            if (currentRoom != null) {
                OutResponse broadcastMsg = (OutResponse) msgFactory.getResponseMessage(MessagesID.OUT);
                switch (aSession.getCurrentZone()) {
                    default:
                        break;
                }

            } else {
                resKickOut.setFailure(ResponseCode.FAILURE,
                        "Bạn đã thoát khỏi bàn!");
            }

        }
        catch (Throwable t) {
            resKickOut.setFailure(ResponseCode.FAILURE, "Bị lỗi kick out");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally {
            if ((resKickOut != null && resKickOut.mCode == ResponseCode.FAILURE)) {
                aResPkg.addMessage(resKickOut);
            }
        }
        return 1;
    }
}
