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
import allinone.protocol.messages.GetItemRequest;
import allinone.protocol.messages.GetItemResponse;

public class GetItemJSON implements IMessageProtocol {


    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            GetItemRequest albRequest = (GetItemRequest)aDecodingObj;
            
            albRequest.cacheVersion = jsonData.getInt("v");
            
        } catch (JSONException ex) {
        }
        return true;
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            GetItemResponse adv = (GetItemResponse) aResponseMessage;
            
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(adv.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (adv.mCode == ResponseCode.FAILURE) {
                 sb.append(adv.message);
            }else {
                sb.append(adv.value);
            }

            encodingObj.put("v", sb.toString());
            return encodingObj;
            
            
            
        } catch (Throwable t) {
            return null;
        }
    }
}
