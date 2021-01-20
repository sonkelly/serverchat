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
import allinone.protocol.messages.AddFriendRequest;
import allinone.protocol.messages.AddFriendResponse;

public class AddFriendJSON implements IMessageProtocol {

    private final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(AddFriendJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            AddFriendRequest addFriend = (AddFriendRequest) aDecodingObj;
            addFriend.friendID = jsonData.getLong("v");
            return true;
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
           
            AddFriendResponse addFriend = (AddFriendResponse) aResponseMessage;
            if(addFriend.session != null && addFriend.session.getByteProtocol()> AIOConstants.PROTOCOL_ADVERTISING)
            {
                StringBuilder sb = new StringBuilder();
                sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Integer.toString(addFriend.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
                //if (addFriend.mCode == ResponseCode.FAILURE) {
                	 sb.append(addFriend.mErrorMsg);
                //}
                encodingObj.put("v", sb.toString());
                return encodingObj;
            }
            
            encodingObj.put("mid", aResponseMessage.getID());
            encodingObj.put("code", addFriend.mCode);
            //if (addFriend.mCode == ResponseCode.FAILURE) {
                encodingObj.put("error_msg", addFriend.mErrorMsg);
//            } else if (addFriend.mCode == ResponseCode.SUCCESS) {
//                encodingObj.put("error_msg", addFriend.mErrorMsg);
//            }
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
