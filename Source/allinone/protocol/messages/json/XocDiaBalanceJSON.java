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
import allinone.protocol.messages.XocDiaBalanceResponse;
import org.json.JSONArray;

public class XocDiaBalanceJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
            XocDiaBalanceJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
            throws ServerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object encode(IResponseMessage aResponseMessage)
            throws ServerException {
        try {

            JSONObject encodingObj = new JSONObject();
            XocDiaBalanceResponse xdBalance = (XocDiaBalanceResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(
                    AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(xdBalance.mCode)).append(
                    AIOConstants.SEPERATOR_NEW_MID);
            if (xdBalance.mCode == ResponseCode.FAILURE) {
                sb.append(xdBalance.errMsg);
            } else {
                sb.append(dataXocDia(xdBalance.refundChan, xdBalance.refundLe));
            }
            mLog.debug("list refund " + sb.toString());
            encodingObj.put("v", sb.toString());
            return encodingObj;

        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }

    private String dataXocDia(JSONArray RefundChan, JSONArray RefundLe) throws JSONException {
        StringBuilder sb = new StringBuilder();

        //end all user info
        //sb.append(AIOConstants.SEPERATOR_BYTE_3);
        for (int i = 0; i < RefundChan.length(); i++) {

            try {
                JSONObject userInfo = RefundChan.getJSONObject(i);

                sb.append(userInfo.get("uid")).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(userInfo.get("money")).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(userInfo.get("totalBetChan")).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(userInfo.get("currentMoney"));

                if (i < RefundChan.length() - 1) {
                    sb.append(AIOConstants.SEPERATOR_BYTE_2);
                }
                mLog.debug(userInfo.get("uid") + " refund chan: " + userInfo.get("money") + " betafter:" + userInfo.get("totalBetChan"));
            } catch (JSONException ex) {

            }
        }
        sb.append(AIOConstants.SEPERATOR_BYTE_3);

        for (int i = 0; i < RefundLe.length(); i++) {
            try {
                JSONObject userInfo = RefundLe.getJSONObject(i);
                sb.append(userInfo.get("uid")).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(userInfo.get("money")).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(userInfo.get("totalBetLe")).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(userInfo.get("currentMoney"));
                if (i < RefundLe.length() - 1) {
                    sb.append(
                            AIOConstants.SEPERATOR_BYTE_2);
                }
                mLog.debug(userInfo.get("uid") + " refund lẻ: " + userInfo.get("money") + " betafter:" + userInfo.get("totalBetLe"));
            } catch (JSONException ex) {

            }
        }
        mLog.debug("Xodai refudndata: " + sb.toString());
        return sb.toString();
    }

}
