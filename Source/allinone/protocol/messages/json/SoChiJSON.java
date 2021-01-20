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
import allinone.protocol.messages.SoChiResponse;

public class SoChiJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
            SoChiJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
            throws ServerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object encode(IResponseMessage aResponseMessage)
            throws ServerException {
        try {
        	
            JSONObject encodingObj = new JSONObject();
            SoChiResponse response = (SoChiResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(response.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (response.mCode == ResponseCode.FAILURE) {
                sb.append(response.message);
            } else {
            	sb.append(response.value);
            }
            
            encodingObj.put("v", sb.toString());
            return encodingObj;

        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
