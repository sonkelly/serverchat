package allinone.protocol.messages.json;

import allinone.data.AIOConstants;
import allinone.protocol.messages.XocDiaOwnerRequest;
import allinone.protocol.messages.XocDiaOwnerResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import vn.com.landsoft.common.ILoggerFactory;
import vn.com.landsoft.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;

public class XocDiaOwnerJSON
        implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(XocDiaOwnerJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
            throws ServerException {
        try {
            this.mLog.debug("[XocDiaOwnerJSON DECODER]: ");
            this.mLog.debug("[XocDiaOwnerJSON DECODER2]: " + aEncodedObj.toString());
            JSONObject jsonData = (JSONObject) aEncodedObj;
            XocDiaOwnerRequest req = (XocDiaOwnerRequest) aDecodingObj;

            String[] arrV = jsonData.getString("v").split(AIOConstants.SEPERATOR_BYTE_1);
            req.uid = Long.parseLong(arrV[0]);
            req.isOwner = Integer.parseInt(arrV[1]);
            return true;
        } catch (Throwable t) {
            this.mLog.error("[DECODER] " + aDecodingObj.getID(), t);
        }
        return false;
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage)
            throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            XocDiaOwnerResponse response = (XocDiaOwnerResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(response.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (response.mCode == 0) {
                sb.append(response.message);
            } else {
                sb.append(response.isOwner ? "1" : "0").append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(response.uid).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(response.message);
            }
            encodingObj.put("v", sb.toString());
            return encodingObj;
        } catch (Throwable t) {
            this.mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
        }
        return null;
    }
}
