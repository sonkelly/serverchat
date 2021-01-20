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
import allinone.protocol.messages.ZoneCacheResponse;

public class ZoneCacheJSON implements IMessageProtocol {


    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            ZoneCacheResponse adv = (ZoneCacheResponse) aResponseMessage;
            
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(adv.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (adv.mCode == ResponseCode.FAILURE) {
                 sb.append(adv.message);
            }else {
                sb.append(adv.gameInfo);
            }

            encodingObj.put("v", sb.toString());
            return encodingObj;
            
            
            
        } catch (Throwable t) {
            return null;
        }
    }
}
