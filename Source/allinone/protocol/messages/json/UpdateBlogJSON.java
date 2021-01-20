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
import allinone.protocol.messages.UpdateBlogRequest;
import allinone.protocol.messages.UpdateBlogResponse;

public class UpdateBlogJSON implements IMessageProtocol {

    private final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(UpdateBlogJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            UpdateBlogRequest addAvatar = (UpdateBlogRequest) aDecodingObj;
            String value = jsonData.getString("v");
            String[] arr = value.split(AIOConstants.SEPERATOR_BYTE_1);
            addAvatar.blogId = Long.parseLong(arr[0]);
            addAvatar.title = arr[1];
            addAvatar.subject = arr[2];
            addAvatar.post = arr[3];
            
            
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
            
            // cast response obj
            UpdateBlogResponse res = (UpdateBlogResponse) aResponseMessage;
            
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(res.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            
            if (res.mCode == ResponseCode.FAILURE) {
                     sb.append(res.mErrorMsg);
            }
            
            // response encoded obj
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
