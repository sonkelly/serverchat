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
import allinone.protocol.messages.UpdateUserInfoRequest;
import allinone.protocol.messages.UpdateUserInfoResponse;

public class UpdateUserInfoJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(UpdateUserInfoJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            UpdateUserInfoRequest update = (UpdateUserInfoRequest) aDecodingObj;
            
            	String s = jsonData.getString("v");
            	String[] arr = s.split(AIOConstants.SEPERATOR_BYTE_1);
            	if(arr.length == 3) {	
            		update.name = arr[0];
            		update.cmt = arr[1];
            		update.address = arr[2];
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
            UpdateUserInfoResponse update = (UpdateUserInfoResponse) aResponseMessage;

            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(update.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            
            if (update.mCode == ResponseCode.FAILURE) {
            	 sb.append(update.mErrorMsg);
            } else {
            	 sb.append(update.mess);
            }
            
            encodingObj.put("v", sb.toString());
            return encodingObj;

        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
