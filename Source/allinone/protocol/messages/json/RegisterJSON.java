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
import allinone.protocol.messages.RegisterRequest;
import allinone.protocol.messages.RegisterResponse;

public class RegisterJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
            RegisterJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
            throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            RegisterRequest register = (RegisterRequest) aDecodingObj;

            if (jsonData.has("v")) {

                String[] arrValues = jsonData.getString("v").split(AIOConstants.SEPERATOR_BYTE_1);
                register.mUsername = arrValues[0];
                register.mPassword = arrValues[1];
                try {
                    register.partnerId = Integer.parseInt(arrValues[2]);
                
                    if ("".equals(arrValues[4])) {
                    	register.deviceType = 4;
                    } else {
                    	register.deviceType = Integer.parseInt(arrValues[4]); // 1 android, 2 iphone, 3, winphone, 4 ko xac dinh
                    }
                    register.clientSessionId 	= arrValues[5];
                    register.registerTime 		= Integer.parseInt(arrValues[6]);
                    register.deviceUID 			= arrValues[7];
                    try {
                    	register.phone 				= arrValues[8];
                    } catch (Exception ex) {
                    	register.phone 				= "";
                    }
                    } catch (Exception ex) {
                    	
                    }
                return true;
            }
            return false;
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    public Object encode(IResponseMessage aResponseMessage)
            throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            RegisterResponse register = (RegisterResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(register.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (register.mCode == ResponseCode.FAILURE) {
                sb.append(register.mErrorMsg);
            }
            sb.append(register.values);
            encodingObj.put("v", sb.toString());
            return encodingObj;

        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
