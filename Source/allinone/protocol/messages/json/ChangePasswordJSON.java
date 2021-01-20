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
import allinone.protocol.messages.ChangePasswordRequest;
import allinone.protocol.messages.ChangePasswordResponse;

public class ChangePasswordJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(ChangePasswordJSON.class);
    
    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            ChangePasswordRequest request = (ChangePasswordRequest) aDecodingObj;
            
            String[] arr = jsonData.getString("v").split(AIOConstants.SEPERATOR_BYTE_1);
            if(arr[0] != null)
            request.oldPassword = arr[0];
            if(arr[1] != null)
            request.newPassword = arr[1];
            if(arr[2] != null)
            request.reTypePassword = arr[2];
            
            return true;
            
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }
    
    @Override
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            ChangePasswordResponse response = (ChangePasswordResponse) aResponseMessage;
            
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(response.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (response.mCode == ResponseCode.FAILURE) {
                     sb.append(response.mErrorMsg);
            }
            else
            {
                sb.append(response.value);
            }

            encodingObj.put("v", sb.toString());
            return encodingObj;
            
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
