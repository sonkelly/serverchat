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
import allinone.protocol.messages.GetChatRoomRequest;
import allinone.protocol.messages.GetChatRoomResponse;

/**
 *
 * @author mcb
 */
public class GetChatRoomJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(GetChatRoomJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
     try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            GetChatRoomRequest albRequest = (GetChatRoomRequest)aDecodingObj;
            String v = jsonData.getString("v");
            if(v!= null && !v.equals(""))
               albRequest.cacheVersion = jsonData.getInt("v");
            
            
            return true;
        } catch (JSONException ex) {
            mLog.error(ex.getMessage(), ex);
        }
        return true;
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            
            
            GetChatRoomResponse res = (GetChatRoomResponse) aResponseMessage; 
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(res.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            
            if (res.mCode == ResponseCode.FAILURE) {
                     sb.append(res.errMsg);
            }else {
                    if(res.value != null && !res.value.equals(""))
                    {
                        sb.append(res.value);
                    }
            }
            
            
            encodingObj.put("v", sb.toString());
            
            return encodingObj;
        } catch (JSONException ex) {
            mLog.error(ex.getMessage(), ex);
        }
        
        return null;
    }
}