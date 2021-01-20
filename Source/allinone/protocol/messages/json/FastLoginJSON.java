package allinone.protocol.messages.json;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.MessagesID;
import allinone.data.ResponseCode;
import allinone.protocol.messages.FastLoginRequest;
import allinone.protocol.messages.FastLoginResponse;
import org.json.JSONException;

public class FastLoginJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(FastLoginJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            FastLoginRequest login = (FastLoginRequest) aDecodingObj;

            if (jsonData.has("v")) {
                try {
                    String v = jsonData.getString("v");
                    String[] arrValues;
                    arrValues = v.split(AIOConstants.SEPERATOR_BYTE_1);
                    int arrSize = arrValues.length;
                    mLog.debug("arrSize:" + arrSize);
                    login.deviceID = arrValues[0];
                    try {
                        login.deviceType = Integer.parseInt(arrValues[1]);
                    }catch(Exception ex) {}
                    mLog.debug("deviceID" + login.deviceID);
                    login.mobileVersion = arrValues[2];

                } catch (JSONException ex) {
                    mLog.error(ex.getMessage(), ex);
                }
            }
            return true;
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

//    private void getMobileLoginInfo(FastLoginResponse login, StringBuilder sb) {
//        if (login.mCode == ResponseCode.SUCCESS) {
//            sb.append(login.mUid).append(AIOConstants.SEPERATOR_BYTE_1);
//            sb.append(login.usrEntity.mUsername).append(AIOConstants.SEPERATOR_BYTE_1);
//            sb.append(Long.toString(login.money)).append(AIOConstants.SEPERATOR_BYTE_1);
//            sb.append(login.password).append(AIOConstants.SEPERATOR_BYTE_1);
//            sb.append(login.expr).append(AIOConstants.SEPERATOR_BYTE_1);
//            sb.append(login.isPhoneUpdate ? 1 : 0).append(AIOConstants.SEPERATOR_BYTE_1);
//            sb.append(login.chkEvent ? 1 : 0).append(AIOConstants.SEPERATOR_BYTE_1);
//            sb.append(login.chkEmail ? 1 : 0).append(AIOConstants.SEPERATOR_BYTE_1);
//            sb.append(login.alertEmailContent).append(AIOConstants.SEPERATOR_BYTE_1);
//            sb.append(login.alertEmailTitle);
//            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.viewname);
//            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.avatarID);
//            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.isPayment ? 1 : 0);
//            if (login.zone_id > 0 && login.lastRoom > 0) {
//                sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.zone_id);
//                sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.lastRoom);
//            }
//
//        } else {
//            sb.append(login.mErrorMsg);
//        }
//    }
    private void getMobileLoginInfo(FastLoginResponse login, StringBuilder sb) {
        if (login.mCode == ResponseCode.SUCCESS) {
            sb.append(login.mUid).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(login.money);
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.realmoney);
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.expr);
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.chkEvent ? 1 : 0);
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.chkEmail ? 1 : 0);
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.alertEmailContent);
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.alertEmailTitle);
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.isPhoneUpdate ? 1 : 0);
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.viewname);
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.avatarID);
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.isPayment ? 1 : 0);
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.isActive ? 1 : 0);
            if (login.zone_id > 0 && login.lastRoom > 0) {
                sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.zone_id);
                sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.lastRoom);
            }

        } else {
            sb.append(login.mErrorMsg);
        }
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {

            JSONObject encodingObj = new JSONObject();
            FastLoginResponse login = (FastLoginResponse) aResponseMessage;

            if (login.session != null) {
                StringBuilder valueSb = new StringBuilder();
                valueSb.append(Integer.toString(MessagesID.FAST_LOGIN)).append(AIOConstants.SEPERATOR_BYTE_1);
                valueSb.append(Integer.toString(login.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
                getMobileLoginInfo(login, valueSb);
                encodingObj.put("v", valueSb.toString());
            }

            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
