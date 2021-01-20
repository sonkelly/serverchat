/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import allinone.protocol.messages.DeleteMessageRequest;
import allinone.protocol.messages.DeleteMessageResponse;

/**
 *
 * @author mcb
 */
public class DeleteMessageJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(DeleteMessageJSON.class);
    

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            DeleteMessageRequest newRequest = (DeleteMessageRequest)aDecodingObj;
            String v = jsonData.getString("v");
            String[] arr = v.split(AIOConstants.SEPERATOR_BYTE_1);
            
            
            newRequest.type = Integer.parseInt(arr[0]);
            if(arr.length>0)
                newRequest.messageId = Long.parseLong(arr[1]);
            
            return true;
        } catch (JSONException ex) {
            mLog.error(ex.getMessage(), ex);
            return false;
        }
    }
    
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try
        {
            JSONObject encodingObj = new JSONObject();
            // put response data into json object
            
            // cast response obj
            DeleteMessageResponse res = (DeleteMessageResponse) aResponseMessage;
            
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(res.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            
            if (res.mCode == ResponseCode.FAILURE) {
                     sb.append(res.mErrorMsg);
            }
            
            
            encodingObj.put("v", sb.toString());
            
            
            
            // response encoded obj
            return encodingObj;
        } catch (Throwable t)
        {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
    
}
