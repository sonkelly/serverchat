package allinone.protocol.messages.json;

import allinone.data.AIOConstants;
import allinone.protocol.messages.XocDiaMuaRequest;
import allinone.protocol.messages.XocDiaMuaResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import vn.com.landsoft.common.ILoggerFactory;
import vn.com.landsoft.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;

public class XocDiaMuaJSON
        implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(XocDiaFireJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
            throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            XocDiaMuaRequest req = (XocDiaMuaRequest) aDecodingObj;
            String[] arrV = jsonData.getString("v").split(AIOConstants.SEPERATOR_BYTE_1);
            req.money = Long.parseLong(arrV[0]);
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
            XocDiaMuaResponse response = (XocDiaMuaResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(response.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (response.mCode == 0) {
                sb.append(response.message);
            } else {
                sb.append(response.betSide).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(response.uidBuy).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(response.message);

            }
            this.mLog.debug("XocDia Mua [DECODER] " + sb.toString());
            encodingObj.put("v", sb.toString());
            return encodingObj;
        } catch (Throwable t) {
            this.mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
        }
        return null;
    }
}
