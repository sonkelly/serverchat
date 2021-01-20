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
import allinone.protocol.messages.GetChiecNonRequest;
import allinone.protocol.messages.GetChiecNonResponse;

public class GetChiecNonJSON implements IMessageProtocol {


    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            GetChiecNonRequest albRequest = (GetChiecNonRequest)aDecodingObj;
            String v = jsonData.getString("v");
            if(v!= null && !v.equals(""))
               albRequest.cacheVersion = jsonData.getInt("v");
            
            
            return true;
        } catch (JSONException ex) {
        }
        return true;
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            GetChiecNonResponse adv = (GetChiecNonResponse) aResponseMessage;
            
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
