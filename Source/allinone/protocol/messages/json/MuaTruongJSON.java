package allinone.protocol.messages.json;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;
import allinone.protocol.messages.MuaTruongRequest;
import allinone.protocol.messages.MuaTruongResponse;

public class MuaTruongJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(MuaTruongJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            // request data
            JSONObject jsonData = (JSONObject) aEncodedObj;
            // plain obj
            MuaTruongRequest mua = (MuaTruongRequest) aDecodingObj;
            // decoding
            mua.matchID = jsonData.getLong("match_id");

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
            MuaTruongResponse mua = (MuaTruongResponse) aResponseMessage;
            encodingObj.put("code", mua.mCode);
            // System.out.println(" chat.mUsername : " +  chat.mUsername);
            if (mua.mCode == ResponseCode.FAILURE) {
                encodingObj.put("error_msg", mua.mErrorMsg);
            } else if (mua.mCode == ResponseCode.SUCCESS) {
                encodingObj.put("uid", mua.mUid);

            }
            // response encoded obj
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
