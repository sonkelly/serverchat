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
import allinone.protocol.messages.UpdatePhoneRequest;
import allinone.protocol.messages.UpdatePhoneResponse;

public class UpdatePhoneJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(UpdatePhoneJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        
    	try {
	            JSONObject jsonData = (JSONObject) aEncodedObj;
	            UpdatePhoneRequest update = (UpdatePhoneRequest) aDecodingObj;
	        	String s = jsonData.getString("v");
	        	
	        	String[] arr = s.split(AIOConstants.SEPERATOR_BYTE_1);
	        	
	        	try {
	        		update.phone = arr[0];
	        	} catch(Exception ex) {
	        		update.phone = "";
	        	}
	        		        	
	        	try {
	        		update.userName = arr[1];
	        	} catch(Exception ex) {
	        		update.userName = "";
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
            UpdatePhoneResponse update = (UpdatePhoneResponse) aResponseMessage;

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
