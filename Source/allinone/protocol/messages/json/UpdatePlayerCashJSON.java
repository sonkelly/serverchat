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
import allinone.protocol.messages.UpdatePlayerCashRequest;
import allinone.protocol.messages.UpdatePlayerCashResponse;

public class UpdatePlayerCashJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(UpdatePlayerCashJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        
    	try {
	            JSONObject jsonData = (JSONObject) aEncodedObj;
	            UpdatePlayerCashRequest update = (UpdatePlayerCashRequest) aDecodingObj;
	            String s = jsonData.getString("v");
	        	
	        	String[] arr = s.split(AIOConstants.SEPERATOR_BYTE_1);
	        	update.matchId = Long.parseLong(arr[0]);
	        	update.cash = Long.parseLong(arr[1]);
	            
	        	return true;
            
    	} catch (Throwable t) {
	            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
	            return false;
    	}
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
        	
            JSONObject encodingObj = new JSONObject();
            UpdatePlayerCashResponse update = (UpdatePlayerCashResponse) aResponseMessage;

            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            
            sb.append(Integer.toString(update.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            
            if (update.mCode == ResponseCode.FAILURE) {
            	 sb.append(update.mErrorMsg);
            } else {
            	 sb.append(update.userId).append(AIOConstants.SEPERATOR_BYTE_1);
            	 sb.append(update.cash);
            }
            
            encodingObj.put("v", sb.toString());
            return encodingObj;

        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
