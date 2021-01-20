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

import allinone.protocol.messages.XocDiaHistoryResponse;
import java.util.ArrayList;

import org.json.JSONArray;

public class XocDiaHistoryJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
            XocDiaHistoryJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
            throws ServerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage)
            throws ServerException {
        try {

            JSONObject encodingObj = new JSONObject();
            XocDiaHistoryResponse xdBalance = (XocDiaHistoryResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(
                    AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(xdBalance.mCode));
            if(xdBalance.oldResult2.size() > 0){
                sb.append(AIOConstants.SEPERATOR_NEW_MID);
            }
            if (xdBalance.mCode == ResponseCode.FAILURE) {
                sb.append(xdBalance.errMsg);
            } else {
                sb.append(dataXocDiaOldResult(xdBalance.oldResult2));
            }
            sb.append(AIOConstants.SEPERATOR_BYTE_2).append(xdBalance.phienID);
            //mLog.debug("list refund " + sb.toString());
            encodingObj.put("v", sb.toString());
            return encodingObj;

        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }

    private String dataXocDiaOldResult(ArrayList<String> _oldResult) throws JSONException {
        StringBuilder sb = new StringBuilder();

        //end all user info
        if (_oldResult.size() > 0) {
            for (int i = 0; i < _oldResult.size(); i++) {
                sb.append(_oldResult.get(i));
                //mLog.debug("Xodai _oldResult: " + _oldResult.get(i));

                if (i < _oldResult.size() - 1) {
                    sb.append(AIOConstants.SEPERATOR_BYTE_1);
                }

            }
        }
        //mLog.debug("Xodai refudndata: " + sb.toString());
        return sb.toString();
    }

}
