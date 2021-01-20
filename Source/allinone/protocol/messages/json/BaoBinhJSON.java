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
import allinone.protocol.messages.BaoBinhRequest;
import allinone.protocol.messages.BaoBinhResponse;

public class BaoBinhJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(BaoBinhJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {

    	try {
    		
            // request data
            JSONObject jsonData = (JSONObject) aEncodedObj;
            BaoBinhRequest request = (BaoBinhRequest) aDecodingObj;

            try {
                
            	String v = jsonData.getString("v");
                String[] arr = v.split(AIOConstants.SEPERATOR_BYTE_1);
                
                if(arr.length >= 2) {
                	request.matchID = Long.parseLong(arr[0]);
                	request.isBao = (Integer.parseInt(arr[1]) == 1)? true : false;
                }
                
                if(arr.length >= 3) {
                	request.cardList = arr[2];
                } 

                return true;
            } catch (Exception ex) {
                mLog.error(ex.getMessage(), ex);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        
    	try {
        	
            JSONObject encodingObj = new JSONObject();
            BaoBinhResponse response = (BaoBinhResponse) aResponseMessage;

            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(response.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            
            if (response.mCode == ResponseCode.FAILURE) {
                sb.append(response.message);
            } else {
                sb.append(response.uid).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(response.binhType);
            }
            
            encodingObj.put("v", sb.toString());
            
            return encodingObj;
            
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    	
    }
}
