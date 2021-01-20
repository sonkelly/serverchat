/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages.json;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.MessagesID;
import allinone.data.ResponseCode;
import allinone.protocol.messages.PairingPlayersRequest;
import allinone.protocol.messages.PairingPlayersResponse;
import org.json.JSONException;

/**
 *
 * @author binh_lethanh
 */
public class PairingPlayersJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(PairingPlayersJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
            throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            PairingPlayersRequest pairingPlayersRequest = (PairingPlayersRequest) aDecodingObj;

            String v = jsonData.getString("v");
            String[] arrValues = v.split(AIOConstants.SEPERATOR_BYTE_1);

            pairingPlayersRequest.uid = Integer.parseInt(arrValues[0]);
            pairingPlayersRequest.zoneId = Integer.parseInt(arrValues[1]);
            pairingPlayersRequest.money = Long.parseLong(arrValues[2]);
            pairingPlayersRequest.cancelPairing = Boolean.parseBoolean(arrValues[3]);
            return true;

        } catch (NumberFormatException | JSONException t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    private String getMidNew(PairingPlayersResponse pairingPlayersResponse) {
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toString(MessagesID.MSG_PAIRING_PLAYERS)).append(AIOConstants.SEPERATOR_BYTE_1);
        sb.append(Integer.toString(pairingPlayersResponse.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);

        if (pairingPlayersResponse.mCode == ResponseCode.SUCCESS) {

            sb.append(pairingPlayersResponse.message);

        } else {
            sb.append(pairingPlayersResponse.mErrorMsg);
        }

        return sb.toString();

    }

    @Override
    public Object encode(IResponseMessage aResponseMessage)
            throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            PairingPlayersResponse pairingPlayersResponse = (PairingPlayersResponse) aResponseMessage;
            encodingObj.put("v", getMidNew(pairingPlayersResponse));
            return encodingObj;

        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
