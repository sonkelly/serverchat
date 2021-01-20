package allinone.protocol.messages.json;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;
import allinone.protocol.messages.AnnounceRequest;
import allinone.protocol.messages.AnnounceResponse;

public class AnnounceJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(AnnounceJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            AnnounceRequest anno = (AnnounceRequest) aDecodingObj;
            anno.message = jsonData.getString("Message");
            return true;
        } catch (Throwable t) {
            mLog.error("[DECODER] ", t);
            return false;
        }
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {            
            JSONObject encodingObj = new JSONObject();
            encodingObj.put("mid", aResponseMessage.getID());
            AnnounceResponse anno = (AnnounceResponse) aResponseMessage;
            encodingObj.put("code", anno.mCode);
            if (anno.mCode == ResponseCode.FAILURE) {
            } else if (anno.mCode == ResponseCode.SUCCESS) {
                encodingObj.put("message", anno.message);
            }
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
