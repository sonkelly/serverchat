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
import allinone.protocol.messages.GetBonusCashRequest;
import allinone.protocol.messages.GetBonusCashResponse;

public class GetBonusCashJSON implements IMessageProtocol {

	private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
			GetBonusCashJSON.class);

	public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
			throws ServerException {
		
		 try {
	            JSONObject jsonData = (JSONObject) aEncodedObj;
	            GetBonusCashRequest request = (GetBonusCashRequest) aDecodingObj;
	            if(jsonData.has("v")){
	            	String s = jsonData.getString("v");
	            	String[] arr 		= s.split(AIOConstants.SEPERATOR_BYTE_1);
	            	int size = arr.length;
	            	request.type 		= Integer.parseInt(arr[0]);
	            	request.socialId 	= arr[1];
	            	request.deviceUid 	= arr[2];
	            	
	            	if(size >= 4) {
	            		request.eventKey 	= arr[3];
	            	}
	            	
	            	return true;
	            }	            
	        } catch (Throwable t) {
	            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
	            return false;
	        }
		
		return true;
	}
	
	public Object encode(IResponseMessage aResponseMessage)
			throws ServerException {
		try {
			JSONObject encodingObj = new JSONObject();
			
			GetBonusCashResponse response = (GetBonusCashResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(response.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            
            if (response.mCode == ResponseCode.FAILURE) {
                sb.append(response.mErrorMsg);
            } else {
            	sb.append(response.money).append(
    					AIOConstants.SEPERATOR_BYTE_1);
    			sb.append(response.mErrorMsg);
            }
            encodingObj.put("v", sb.toString());
            return encodingObj;
                        
		} catch (Throwable t) {
			mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
			return null;
		}
	}

}
