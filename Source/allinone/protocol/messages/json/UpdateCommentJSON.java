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
import allinone.protocol.messages.UpdateCommentRequest;
import allinone.protocol.messages.UpdateCommentResponse;

/**
 *
 * @author mcb
 */
public class UpdateCommentJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(UpdateCommentJSON.class);
    

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            UpdateCommentRequest newRequest = (UpdateCommentRequest)aDecodingObj;
            String v = jsonData.getString("v");
            String[] arr = v.split(AIOConstants.SEPERATOR_BYTE_1);
            
            newRequest.comment = arr[1];
            
            newRequest.commentId = Long.parseLong(arr[0]);
            
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
            
            // cast response obj
            UpdateCommentResponse res = (UpdateCommentResponse) aResponseMessage;
            
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(res.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            
            if (res.mCode == ResponseCode.FAILURE) {
                     sb.append(res.mErrorMsg);
            }
            
            
            
            // response encoded obj
            return encodingObj;
        } catch (Throwable t)
        {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
    
}
