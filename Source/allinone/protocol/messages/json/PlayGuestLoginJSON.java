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
import allinone.protocol.messages.PlayGuestLoginRequest;
import allinone.protocol.messages.PlayGuestLoginResponse;

public class PlayGuestLoginJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(PlayGuestLoginJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            PlayGuestLoginRequest login = (PlayGuestLoginRequest) aDecodingObj;

            if (jsonData.has("v")) {
                try {
                    String v = jsonData.getString("v");
                    String[] arrValues;
                    arrValues = v.split(AIOConstants.SEPERATOR_BYTE_1);

                    login.deviceUId = arrValues[0];
                    login.partnerId = Integer.parseInt(arrValues[1]);
                    login.refCode =  Integer.parseInt(arrValues[2]);
                    login.mobileVersion = arrValues[3];
                    try {
                    	login.deviceType =  Integer.parseInt(arrValues[4]);
                    } catch (Exception ex) {
                    	login.deviceType = 4; // khong xac dinh loai thiet bi
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

    private void getMobileLoginInfo(PlayGuestLoginResponse login, StringBuilder sb) {
        if (login.mCode == ResponseCode.SUCCESS) {
            sb.append(login.mUid).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(login.usrEntity.mUsername).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Long.toString(login.money)).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(login.password).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(login.expr).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(login.isPhoneUpdate ? 1 : 0).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(login.chkEvent ? 1 : 0).append(AIOConstants.SEPERATOR_BYTE_1);
      	  	sb.append(login.chkEmail ? 1 : 0).append(AIOConstants.SEPERATOR_BYTE_1);
      	  	sb.append(login.alertEmailContent).append(AIOConstants.SEPERATOR_BYTE_1);
      	    sb.append(login.alertEmailTitle).append(AIOConstants.SEPERATOR_BYTE_1);
            
        } else {
            sb.append(login.mErrorMsg);
        }
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        
    	try {
            JSONObject encodingObj = new JSONObject();
            PlayGuestLoginResponse login = (PlayGuestLoginResponse) aResponseMessage;
            
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
}
