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
import allinone.data.ResponseCode;
import allinone.protocol.messages.EndTankMatchRequest;
import allinone.protocol.messages.EndTankMatchResponse;

/**
 *
 * @author Vostro 3450
 */
public class EndTankMatchJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
            EndTankMatchJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
            throws ServerException {
        try {
            // request data
            JSONObject jsonData = (JSONObject) aEncodedObj;
            // request messsage
            EndTankMatchRequest matchTurn = (EndTankMatchRequest) aDecodingObj;
            // parsing
            matchTurn.mMatchId = jsonData.getLong("match_id");

            matchTurn.timeOut = jsonData.getBoolean("timeOut");
            if (matchTurn.timeOut) {
                matchTurn.point = jsonData.getInt("point");
            } else {
                matchTurn.isWin = jsonData.getBoolean("isWin");
                matchTurn.message = jsonData.getString("message");
            }
            return true;
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    public Object encode(IResponseMessage aResponseMessage)
            throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            // put response data into json object
            encodingObj.put("mid", aResponseMessage.getID());
            // cast response obj
            EndTankMatchResponse matchTurn = (EndTankMatchResponse) aResponseMessage;
            encodingObj.put("code", matchTurn.mCode);

            if (matchTurn.mCode == ResponseCode.FAILURE) {
                encodingObj.put("error_msg", matchTurn.mErrorMsg);
            } else if (matchTurn.mCode == ResponseCode.SUCCESS) {
            }
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
