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
import allinone.data.MessagesID;
import allinone.data.ResponseCode;
import allinone.protocol.messages.FastPlayRequest;
import allinone.protocol.messages.FastPlayResponse;

public class FastPlayJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(FastPlayJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            // request data
            JSONObject jsonData = (JSONObject) aEncodedObj;
            FastPlayRequest fast = (FastPlayRequest) aDecodingObj;
            
            if(jsonData.has("v"))
            {
                fast.zoneId = jsonData.getInt("v");
                return true;
                
            }
            
            // plain obj
            
            // decoding
            fast.zoneId = jsonData.getInt("room_id");
            return true;
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }
    private void getMidEncode(FastPlayResponse fast, JSONObject encodingObj) throws JSONException
    {
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toString(MessagesID.FastPlay)).append(AIOConstants.SEPERATOR_BYTE_1);
        sb.append(Integer.toString(fast.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
        if(fast.mCode == ResponseCode.SUCCESS)
        {
            sb.append(Long.toString(fast.matchID)).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(fast.tableID));
        }
        else
        {
            sb.append(fast.message);
        }
        encodingObj.put("v", sb.toString());
    }
    
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            FastPlayResponse fast = (FastPlayResponse) aResponseMessage;
            if(fast.session != null && fast.session.getByteProtocol()>AIOConstants.PROTOCOL_ADVERTISING)
            {
                getMidEncode(fast, encodingObj);
                
                return encodingObj;
            }
            
            // put response data into json object
            encodingObj.put("mid", aResponseMessage.getID());
            
            encodingObj.put("code", fast.mCode);
            // System.out.println(" chat.mUsername : " +  chat.mUsername);
            if (fast.mCode == ResponseCode.FAILURE) {
                encodingObj.put("error_msg", fast.message);
            } else if (fast.mCode == ResponseCode.SUCCESS) {
                encodingObj.put("match_id", fast.matchID);
                encodingObj.put("nTable", fast.tableID);

            }
            // response encoded obj
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
