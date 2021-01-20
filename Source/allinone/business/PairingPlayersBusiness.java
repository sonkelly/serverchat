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
import vn.game.session.ISession;
import allinone.data.MessagesID;
import allinone.data.ResponseCode;
import allinone.protocol.messages.PairingPlayersRequest;
import allinone.protocol.messages.PairingPlayersResponse;
import vn.game.common.CommonUtils;
import vn.game.room.PairingPlayer;
import vn.game.room.PairingPlayersManager;

/**
 *
 * @author binh_lethanh
 */
public class PairingPlayersBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(PairingPlayersBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) throws ServerException {

        MessageFactory msgFactory = aSession.getMessageFactory();
        PairingPlayersResponse pairingPlayersResponse = (PairingPlayersResponse) msgFactory.getResponseMessage(MessagesID.MSG_PAIRING_PLAYERS);
        pairingPlayersResponse.setSession(aSession);

        try {

            //mLog.debug("[JOIN ROOM]: Catch");
            PairingPlayersRequest pairingPlayersRequest = (PairingPlayersRequest) aReqMsg;
//            long id = aSession.getUID();
            if (CommonUtils.checkAccessJoinRoom(pairingPlayersRequest.money, aSession.getUserEntity().level)) {
                PairingPlayer pairingPlayer = new PairingPlayer(aSession, pairingPlayersRequest.uid);
                if (!PairingPlayersManager.checkExitsPlayer(pairingPlayer, pairingPlayersRequest.zoneId, pairingPlayersRequest.money)) {

                    PairingPlayersManager.putMoneyZone(pairingPlayer, pairingPlayersRequest.zoneId, pairingPlayersRequest.money);
                    pairingPlayersResponse.mCode = ResponseCode.SUCCESS;
                    pairingPlayersResponse.message = ResponseCode.SUCCESS + "";
                } else {
                    if (pairingPlayersRequest.cancelPairing) {
                        PairingPlayer player = PairingPlayersManager.getPlayer(pairingPlayer, pairingPlayersRequest.zoneId, pairingPlayersRequest.money);
                        if (player != null) {
                            player.cancelPairing = true;
                            pairingPlayersResponse.mCode = ResponseCode.SUCCESS;
                            pairingPlayersResponse.message = 2 + ""; // đang hủy vui lòng chờ
                        } else {
                            pairingPlayersResponse.mCode = ResponseCode.SUCCESS;
                            pairingPlayersResponse.message = -2 + ""; // hủy không thành công
                        }
                    } else {

                        pairingPlayersResponse.mCode = ResponseCode.FAILURE;
                        pairingPlayersResponse.message = -1 + ""; // ghép đôi không thành công
                    }
                }
                aSession.write(pairingPlayersResponse);
            } else {
                pairingPlayersResponse.mCode = ResponseCode.SUCCESS;
                pairingPlayersResponse.message = -3 + ""; // chưa đủ cấp độ để chơi
                aSession.write(pairingPlayersResponse);
            }

        } catch (Throwable t) {
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
            pairingPlayersResponse.setFailure(ResponseCode.FAILURE, "Lỗi xảy ra");
            aSession.write(pairingPlayersResponse);

        }
        return 1;
    }

}
