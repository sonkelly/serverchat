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
import allinone.protocol.messages.PlaySocialLoginRequest;
import allinone.protocol.messages.PlaySocialLoginResponse;
import com.google.gson.Gson;

public class PlaySocialLoginJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(PlaySocialLoginJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            PlaySocialLoginRequest login = (PlaySocialLoginRequest) aDecodingObj;

            if (jsonData.has("v")) {
                try {
                    String v = jsonData.getString("v");
                    String[] arrValues;
                    arrValues = v.split(AIOConstants.SEPERATOR_BYTE_1);
                    login.socialId = arrValues[0];

                    login.mobileVersion = arrValues[1];
                    try {
                        login.deviceType = Integer.parseInt(arrValues[2]); // 1 android, 2 iphone, 3, winphone, 4 ko xac dinh
                    } catch (Exception ex) {}
                
                    login.token = arrValues[3];
                    login.deviceID = arrValues[4];
                } catch (Exception ex) {
                    mLog.error(ex.getMessage(), ex);
                }
            }
            return true;
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    public void getMobileLoginInfo(PlaySocialLoginResponse login, StringBuilder sb) {
        if (login.mCode == ResponseCode.SUCCESS) {

            sb.append(login.mUid).append(AIOConstants.SEPERATOR_BYTE_1);

            sb.append(Long.toString(login.money)).append(AIOConstants.SEPERATOR_BYTE_1);
            

            sb.append(login.expr).append(AIOConstants.SEPERATOR_BYTE_1);

            sb.append(login.mail).append(AIOConstants.SEPERATOR_BYTE_1);// chkEvent chuyen thanh truong mail trong db
            sb.append(login.level).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(this.getUserInfo(login)).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(login.alertEmailTitle).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(login.isPhoneUpdate ? 1 : 0).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(login.viewname).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(login.usrEntity.mUsername).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(login.isPayment ? 1 : 0).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(login.isActive ? 1 : 0).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(login.newDateLogin).append(AIOConstants.SEPERATOR_BYTE_1);
            //mLog.debug("login.avatarID:"+login.avatarID);
            sb.append(login.avatarID).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(login.realmoney).append(AIOConstants.SEPERATOR_BYTE_1);
//            if (login.zone_id > 0 && login.lastRoom > 0) {
                sb.append(login.zone_id).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(login.lastRoom).append(AIOConstants.SEPERATOR_BYTE_1);
//            }
            sb.append(login.activeCode);
        } else {
            sb.append(login.mErrorMsg);
        }
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {

            JSONObject encodingObj = new JSONObject();
            PlaySocialLoginResponse login = (PlaySocialLoginResponse) aResponseMessage;

            if (login.session != null) {
                StringBuilder valueSb = new StringBuilder();
                valueSb.append(Integer.toString(100001)).append(AIOConstants.SEPERATOR_BYTE_1);
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
    public String getUserInfo(PlaySocialLoginResponse login){
        //StringBuilder valueSb = new StringBuilder();
        String userinfo = new Gson().toJson(login.usrInfoEntity);
        mLog.debug("userinfo:"+userinfo);
        return userinfo;
    }
}
