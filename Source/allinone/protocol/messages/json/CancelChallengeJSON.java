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
import allinone.data.ResponseCode;
import allinone.protocol.messages.CancelChallengeRequest;
import allinone.protocol.messages.CancelChallengeResponse;

/**
 *
 * @author mcb
 */
public class CancelChallengeJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(CancelChallengeJSON.class);
    

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            CancelChallengeRequest cancelReq = (CancelChallengeRequest)aDecodingObj;
            cancelReq.lstPlayerId = jsonData.getString("lstPlayerId");
            
            return true;
        } catch (JSONException ex) {
            mLog.error(ex.getStackTrace().toString());
            return false;
        }
    }
    
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try
        {
            JSONObject encodingObj = new JSONObject();
            // put response data into json object
            encodingObj.put("mid", aResponseMessage.getID());
            // cast response obj
            CancelChallengeResponse cancel = (CancelChallengeResponse) aResponseMessage;
            encodingObj.put("code", cancel.mCode);
            if(cancel.mCode == ResponseCode.SUCCESS){
                encodingObj.put("msg", cancel.msg);
            }else {
            	encodingObj.put("error", cancel.msg);
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
