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
import allinone.protocol.messages.ItemOrderCancelRequest;
import allinone.protocol.messages.ItemOrderCancelResponse;

public class ItemOrderCancelJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(ItemOrderCancelJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
        	// request data
            JSONObject jsonData = (JSONObject) aEncodedObj;
        	ItemOrderCancelRequest req = (ItemOrderCancelRequest) aDecodingObj;
        	req.itemOrderId = jsonData.getLong("v");
    		
        	return true;

        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
           
            ItemOrderCancelResponse read = (ItemOrderCancelResponse) aResponseMessage;
            
            StringBuilder sb = new StringBuilder();
            
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(read.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            
            if (read.mCode == ResponseCode.FAILURE) {
            	 sb.append(read.mErrorMsg);
            }
            else
            {
                sb.append(read.value);
            }
            
            encodingObj.put("v", sb.toString());
            
            return encodingObj;
            
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
