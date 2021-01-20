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
import allinone.protocol.messages.SetMinBetRequest;
import allinone.protocol.messages.SetMinBetResponse;

/**
 *
 * @author Thomc
 */
public class SetMinBetJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(TimeOutJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
            throws ServerException {
        try {
            JSONObject jsonMinBet = (JSONObject) aEncodedObj;
            SetMinBetRequest rqminbet = (SetMinBetRequest) aDecodingObj;
            rqminbet.moneyBet = jsonMinBet.getLong("moneyBet");
            return true;
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    public Object encode(IResponseMessage aResponseMessage)
            throws ServerException {
        try {
            SetMinBetResponse resMinbet = (SetMinBetResponse) aResponseMessage;
            JSONObject encodingObj = new JSONObject();
            encodingObj.put("mid", resMinbet.getID());
            encodingObj.put("code", resMinbet.mCode);
            encodingObj.put("moneyBet", resMinbet.moneyBet);
            if (resMinbet.mCode == ResponseCode.FAILURE) {
                encodingObj.put("error_msg", resMinbet.errMgs);
            }
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
