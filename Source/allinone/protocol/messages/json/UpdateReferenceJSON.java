package allinone.protocol.messages.json;

import allinone.data.AIOConstants;
import allinone.protocol.messages.UpdateReferenceRequest;
import allinone.protocol.messages.UpdateReferenceResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import vn.game.common.ILoggerFactory;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;

public class UpdateReferenceJSON
        implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(UpdateReferenceJSON.class);

    public UpdateReferenceJSON() {
    }

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            UpdateReferenceRequest update = (UpdateReferenceRequest) aDecodingObj;
            String s = jsonData.getString("v");
            String[] arr = s.split(AIOConstants.SEPERATOR_BYTE_1);
            try {
                update.refrenceCode = arr[0];
            } catch (Exception ex) {
                update.refrenceCode = "";
            }
            return true;
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
        }
        return false;
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            UpdateReferenceResponse update = (UpdateReferenceResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(update.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (update.mCode == 0) {
                sb.append(update.mErrorMsg);
            } else {
                sb.append(update.mess).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(update.newMoney);
            }
            encodingObj.put("v", sb.toString());
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
        }
        return null;
    }
}
