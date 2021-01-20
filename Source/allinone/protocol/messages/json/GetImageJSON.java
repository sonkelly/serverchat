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
import allinone.protocol.messages.GetImageRequest;
import allinone.protocol.messages.GetImageResponse;

public class GetImageJSON  implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(GetImageJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            GetImageRequest getI = (GetImageRequest) aDecodingObj;
            if(jsonData.has("v"))
               getI.name = jsonData.getString("v");
            else
               getI.name = jsonData.getString("name"); 
            return true;
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            GetImageResponse chat = (GetImageResponse) aResponseMessage;
            if(chat.session != null && chat.session.getByteProtocol()> AIOConstants.PROTOCOL_ADVERTISING)
            {
                StringBuilder sb = new StringBuilder();
                sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Integer.toString(chat.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
                if (chat.mCode == ResponseCode.FAILURE) {
                	 sb.append(chat.mErrorMsg);
                }else {
                	sb.append(chat.name).append(AIOConstants.SEPERATOR_BYTE_1);
                	sb.append(chat.image);
                }
                encodingObj.put("v", sb.toString());
                return encodingObj;
            }
            encodingObj.put("code", chat.mCode);
            encodingObj.put("mid", aResponseMessage.getID());
            if (chat.mCode == ResponseCode.FAILURE) {
                encodingObj.put("error_msg", chat.mErrorMsg);
            } else if (chat.mCode == ResponseCode.SUCCESS) {
                encodingObj.put("name", chat.name);
                encodingObj.put("image", chat.image);
            }
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }

}
