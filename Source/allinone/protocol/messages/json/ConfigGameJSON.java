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

import allinone.protocol.messages.ConfigGameResponse;

public class ConfigGameJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
            ConfigGameJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
            throws ServerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object encode(IResponseMessage aResponseMessage)
            throws ServerException {
        try {

            JSONObject encodingObj = new JSONObject();
            ConfigGameResponse xdBalance = (ConfigGameResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(
                    AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(xdBalance.mCode)).append(
                    AIOConstants.SEPERATOR_NEW_MID);
            if (xdBalance.mCode == ResponseCode.FAILURE) {
                sb.append(xdBalance.errMsg);
            } else {
                sb.append(xdBalance.value);
            }
            //mLog.debug("config game result " + sb.toString());
            encodingObj.put("v", sb.toString());
            return encodingObj;

        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }

}
