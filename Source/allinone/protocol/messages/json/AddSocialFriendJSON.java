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
import allinone.protocol.messages.AddSocialFriendRequest;
import allinone.protocol.messages.AddSocialFriendResponse;

public class AddSocialFriendJSON implements IMessageProtocol {

    private final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(AddSocialFriendJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            AddSocialFriendRequest addFriend = (AddSocialFriendRequest) aDecodingObj;
            String v = jsonData.getString("v");
            String[] arr = v.split(AIOConstants.SEPERATOR_BYTE_1);
            
            addFriend.friendID = Integer.parseInt(arr[0]);
            if(arr.length>1)
                addFriend.isConfirmed = arr[1].equals("1");
            else
                addFriend.isConfirmed = true;
            
            
            return true;
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            AddSocialFriendResponse addFriend = (AddSocialFriendResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(addFriend.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            
            if (addFriend.mCode == ResponseCode.FAILURE) {
                     sb.append(addFriend.mErrorMsg);
            }
            else
            {
                sb.append(addFriend.value);
            }
            
            
            encodingObj.put("v", sb.toString());
            
            
            // response encoded obj
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
