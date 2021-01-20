package allinone.protocol.messages.json;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.ResponseCode;
import allinone.protocol.messages.BotFastPlayResponse;
import allinone.protocol.messages.CheckSessionRequest;
import allinone.protocol.messages.CheckSessionResponse;

public class CheckSessionJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(CheckSessionJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            // request data
            JSONObject jsonData = (JSONObject) aEncodedObj;
            CheckSessionRequest fast = (CheckSessionRequest) aDecodingObj;
            
            
            fast.ip = jsonData.getString("v");
            return true;
                
            
            
            // plain obj
            
            // decoding
            
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
        
    }
    private void getMidEncode(BotFastPlayResponse fast, JSONObject encodingObj) throws JSONException
    {
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toString(fast.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
        sb.append(Integer.toString(fast.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
        if(fast.mCode == ResponseCode.FAILURE)
        {
            sb.append(fast.message);
        }
        
        encodingObj.put("v", sb.toString());
    }
    
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            CheckSessionResponse fast = (CheckSessionResponse) aResponseMessage;
            
             StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(fast.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(fast.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if(fast.mCode == ResponseCode.FAILURE)
            {
                sb.append(fast.errMsg);
            }
            else
            {
                sb.append(fast.value);
            }
        
            encodingObj.put("v", sb.toString());
                
                return encodingObj;
            
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
