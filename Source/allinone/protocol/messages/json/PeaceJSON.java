package allinone.protocol.messages.json;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;
import allinone.protocol.messages.PeaceRequest;
import allinone.protocol.messages.PeaceResponse;

/**
 *
 * @author Dinhpv
 */
public class PeaceJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(BuyAvatarJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            // request data
            JSONObject jsonData = (JSONObject) aEncodedObj;
            // cancel request message
            PeaceRequest buy = (PeaceRequest) aDecodingObj;
            // decode
            try {
                buy.mMatchId = jsonData.getInt("match_id");
                buy.uid = jsonData.getLong("uid");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            // put response data into json object
            encodingObj.put("mid", aResponseMessage.getID());
            // cast response obj
            PeaceResponse buy = (PeaceResponse) aResponseMessage;
            encodingObj.put("code", buy.mCode);
            if (buy.mCode == ResponseCode.FAILURE) {
                encodingObj.put("error_msg", buy.mErrorMsg);

            } else if (buy.mCode == ResponseCode.SUCCESS) {
                encodingObj.put("uid", buy.uid);
            }
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
