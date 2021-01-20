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
import allinone.protocol.messages.LoginRequest;
import allinone.protocol.messages.LoginResponse;
import com.google.gson.Gson;

public class LoginJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(LoginJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            // request data
            JSONObject jsonData = (JSONObject) aEncodedObj;
            LoginRequest login = (LoginRequest) aDecodingObj;

            if (jsonData.has("v")) {
                try {
                    String v = jsonData.getString("v");
                    String[] arrValues;
                    arrValues = v.split(AIOConstants.SEPERATOR_BYTE_1);
                    int arrSize = arrValues.length;
                    //mLog.debug("arrSize:" + arrSize);
                    if (arrSize > 4) {
                        //mobile version
                        login.mUsername = arrValues[0];
                        login.mPassword = arrValues[1];
                        login.mobileVersion = arrValues[2];
                        login.device = arrValues[3];
                        login.deviceId = arrValues[4];
                        //login.isMxh = arrValues[4].equals("1");

//                        login.partnerId = Integer.parseInt(arrValues[4]);
//                        if (arrSize > 6) {
//                            try {
//
//                                login.refCode = arrValues[5];
//
//                                mLog.debug("Login Refcode " + login.refCode);
//
//                                //contain game position
//                                login.isMxh = arrValues[6].equals("1");
//
//                                login.gamePosition = Integer.parseInt(arrValues[7]);
//
//                                login.device = arrValues[8];
//
//                                if (arrSize > 9) {
//                                    login.deviceId = Long.parseLong(arrValues[9]);
//                                }
//
//                            } catch (Exception ex) {
//                            }
//                        }
//                        mLog.debug("login:", login);
//                        mLog.debug("login.mobileVersion:", login.mobileVersion);
//                        mLog.debug("login.device:", login.device);
//                        mLog.debug("login.deviceId:", login.deviceId);
                        return true;

                    } else if (arrValues.length == 4) {//dung cho web
                        //flash version
                        login.mUsername = arrValues[0];
                        login.mPassword = arrValues[1];
                        login.protocol = Integer.parseInt(arrValues[2]);
                        login.zoneId = Integer.parseInt(arrValues[3]);
                        return true;
                    }

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

    private void getMobileLoginInfo(LoginResponse login, StringBuilder sb) {
        if (login.mCode == ResponseCode.SUCCESS) {
            sb.append(login.mUid).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(login.money);
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.expr);
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.mail);//chuyen thanh truong mail trong db
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.level);
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(this.getUserInfo(login));
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.alertEmailTitle);
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.isPhoneUpdate ? 1 : 0);
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.viewname);
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.avatarID);
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.isPayment ? 1 : 0);
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.isActive ? 1 : 0);
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.newDateLogin);
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.realmoney);
           
            //if (login.zone_id > 0 && login.lastRoom > 0) {
                sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.zone_id);
                sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.lastRoom);
            //}
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(login.activeCode);

        } else {
            sb.append(login.mErrorMsg);
        }
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            LoginResponse login = (LoginResponse) aResponseMessage;

            StringBuilder valueSb = new StringBuilder();
            valueSb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            valueSb.append(Integer.toString(login.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);

            getMobileLoginInfo(login, valueSb);

            encodingObj.put("v", valueSb.toString());

            return encodingObj;

        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
    public String getUserInfo(LoginResponse login){
        //StringBuilder valueSb = new StringBuilder();
        String userinfo = new Gson().toJson(login.usrInfoEntity);
        mLog.debug("userinfo:"+userinfo);
        return userinfo;
    }
}
