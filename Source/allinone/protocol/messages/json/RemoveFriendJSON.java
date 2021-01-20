package allinone.protocol.messages.json;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.ResponseCode;
import allinone.protocol.messages.RemoveFriendRequest;
import allinone.protocol.messages.RemoveFriendResponse;

public class RemoveFriendJSON implements IMessageProtocol {

    private final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(RemoveFriendJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            RemoveFriendRequest removeFriend = (RemoveFriendRequest) aDecodingObj;
            if(jsonData.has("v")) {
            	removeFriend.friendID = jsonData.getLong("v");
            	return true;
            }
            removeFriend.currID = jsonData.getLong("uid");
            removeFriend.friendID = jsonData.getLong("friend_uid");
            return true;
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            
            RemoveFriendResponse removeFriend = (RemoveFriendResponse) aResponseMessage;
            if(removeFriend.session != null && removeFriend.session.getByteProtocol()> AIOConstants.PROTOCOL_ADVERTISING)
            {
                StringBuilder sb = new StringBuilder();
                sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Integer.toString(removeFriend.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
                if (removeFriend.mCode == ResponseCode.FAILURE) {
                	 sb.append(removeFriend.mErrorMsg);
                }
                encodingObj.put("v", sb.toString());
                return encodingObj;
            }
            encodingObj.put("mid", aResponseMessage.getID());
            encodingObj.put("code", removeFriend.mCode);
            if (removeFriend.mCode == ResponseCode.FAILURE) {
                encodingObj.put("error_msg", removeFriend.mErrorMsg);
            } else if (removeFriend.mCode == ResponseCode.SUCCESS) {
            }
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
