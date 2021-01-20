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
import allinone.protocol.messages.QuayCNKDResponse;

/**
 *
 * @author mcb
 */
public class QuayCNKDJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(QuayCNKDJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
//        try {
//            JSONObject jsonData = (JSONObject) aEncodedObj;
//            BuyItemRequest albRequest = (BuyItemRequest)aDecodingObj;
//
//            albRequest.itemId = jsonData.getInt("v");
//            
//        } catch (JSONException ex) {
//            mLog.error(ex.getMessage(), ex);
//        }
        return true;
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            
            QuayCNKDResponse res = (QuayCNKDResponse) aResponseMessage; 
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(res.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            System.out.println("res.moneyWin:"+res.moneyWin);
            if (res.mCode == ResponseCode.FAILURE) {
                sb.append(res.errMessage).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(res.moneyWin).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(res.count);
            }
            else
            {
                sb.append(res.msg).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(res.moneyWin).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(res.count);
            }
                    
            
            
            encodingObj.put("v", sb.toString());
            
            return encodingObj;
        } catch (JSONException ex) {
            mLog.error(ex.getMessage(), ex);
        }
        
        return null;
    }
}