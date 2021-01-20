package allinone.protocol.messages.json;

import allinone.data.AIOConstants;
import allinone.protocol.messages.XocDiaDuoiCuaRequest;
import allinone.protocol.messages.XocDiaDuoiCuaResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import vn.com.landsoft.common.ILoggerFactory;
import vn.com.landsoft.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;

public class XocDiaDuoiCuaJSON
        implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(XocDiaFireJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
            throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            XocDiaDuoiCuaRequest req = (XocDiaDuoiCuaRequest) aDecodingObj;
            String[] arrV = jsonData.getString("v").split(AIOConstants.SEPERATOR_BYTE_1);
            req.betside = Integer.parseInt(arrV[0]);
            return true;
        } catch (Throwable t) {
            this.mLog.error("[DECODER] " + aDecodingObj.getID(), t);
        }
        return false;
    }

    public Object encode(IResponseMessage aResponseMessage)
            throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            XocDiaDuoiCuaResponse response = (XocDiaDuoiCuaResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(response.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            //if (response.mCode == 0) {
            sb.append(response.message);
//            } else {
//                sb.append(response.isOwner);
//
//            }
            encodingObj.put("v", sb.toString());
            return encodingObj;
        } catch (Throwable t) {
            this.mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
        }
        return null;
    }
}
