package allinone.protocol.messages.json;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.ResponseCode;
import allinone.protocol.messages.FreeTopupResponse;

public class FreeTopupJSON implements IMessageProtocol {


    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        return true;
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            
            FreeTopupResponse freeTopup = (FreeTopupResponse) aResponseMessage;
            
            
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(freeTopup.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            sb.append(Integer.toString(freeTopup.times));

            if (freeTopup.mCode == ResponseCode.FAILURE) {
                     sb.append(AIOConstants.SEPERATOR_BYTE_1).append(freeTopup.message);
            }
                
                encodingObj.put("v", sb.toString());
                return encodingObj;
        } catch (Throwable t) {
            return null;
        }
            
            
    }
}
