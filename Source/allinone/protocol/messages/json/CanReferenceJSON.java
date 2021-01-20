package allinone.protocol.messages.json;

import allinone.data.AIOConstants;
import allinone.protocol.messages.CanReferenceResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import vn.game.common.ILoggerFactory;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;

public class CanReferenceJSON implements vn.game.protocol.IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(CanReferenceJSON.class);

    public CanReferenceJSON() {
    }

    public boolean decode(Object paramObject, IRequestMessage paramIRequestMessage) throws ServerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            CanReferenceResponse response = (CanReferenceResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(response.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (response.mCode == 0) {
                sb.append(response.message);
            } else {
                if (response.canReference) {
                    sb.append(1);
                } else {
                    sb.append(0);
                }
                sb.append(AIOConstants.SEPERATOR_BYTE_1);
                if (response.ispartner) {
                    sb.append(1);
                } else {
                    sb.append(0);
                }
                sb.append(AIOConstants.SEPERATOR_BYTE_1);
                if (response.haveMob) {
                    sb.append(1);
                } else {
                    sb.append(0).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(response.sms_active);
                }
            }
            encodingObj.put("v", sb.toString());

            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
        }
        return null;
    }
}
