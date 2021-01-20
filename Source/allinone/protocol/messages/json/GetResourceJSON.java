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
import allinone.protocol.messages.GetResourceRequest;
import allinone.protocol.messages.GetResourceResponse;

public class GetResourceJSON implements IMessageProtocol {


    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            GetResourceRequest albRequest = (GetResourceRequest)aDecodingObj;
            String v = jsonData.getString("v");
            String[] args = v.split(AIOConstants.SEPERATOR_BYTE_1);
            
            albRequest.type = Integer.parseInt(args[0]);
            albRequest.itemId = Integer.parseInt(args[1]);
            
            
            
            
            return true;
        } catch (JSONException ex) {
        }
        return true;
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            GetResourceResponse adv = (GetResourceResponse) aResponseMessage;
            
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
