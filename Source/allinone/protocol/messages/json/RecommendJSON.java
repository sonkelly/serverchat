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
import allinone.protocol.messages.RecommendRequest;
import allinone.protocol.messages.RecommendResponse;

/**
 *
 * @author mcb
 */
public class RecommendJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(RecommendJSON.class);
    

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            RecommendRequest newRequest = (RecommendRequest)aDecodingObj;
            String v = jsonData.getString("v");
            String[] arr = v.split(AIOConstants.SEPERATOR_BYTE_1);
            
            newRequest.isLag = arr[0].equals("1");
            newRequest.isDiffLogin = arr[1].equals("1");
            newRequest.isDesign = arr[2].equals("1");
            newRequest.isErrorGame = arr[3].equals("1");
            newRequest.isHack = arr[4].equals("1");
            
            if(arr.length>5)
                newRequest.content = arr[5];
            
            
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
            RecommendResponse res = (RecommendResponse) aResponseMessage;
            
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(res.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            
            if (res.mCode == ResponseCode.FAILURE) {
                     sb.append(res.mErrorMsg);
            }
//            else
//            {
//                sb.append();
//            }
            
            
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
