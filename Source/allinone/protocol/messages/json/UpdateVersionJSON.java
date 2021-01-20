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
import allinone.protocol.messages.UpdateVersionResponse;

public class UpdateVersionJSON implements IMessageProtocol {


    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
    	
        try {        
        	JSONObject encodingObj = new JSONObject();
            UpdateVersionResponse response = (UpdateVersionResponse) aResponseMessage;
            
            StringBuilder sb = new StringBuilder();
            
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(response.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            
            if (response.mCode == ResponseCode.FAILURE) {
                 sb.append(response.message);
            } else {
                sb.append(response.message).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(response.link).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(response.currentVersion);
            }
            
            encodingObj.put("v", sb.toString());
            
            return encodingObj;            
        } catch (Throwable t) {
            return null;
        }
    }
}
