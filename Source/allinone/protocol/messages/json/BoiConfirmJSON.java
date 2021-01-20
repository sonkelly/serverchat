package allinone.protocol.messages.json;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.Couple;
import allinone.data.ResponseCode;
import allinone.protocol.messages.BoiConfirmRequest;
import allinone.protocol.messages.BoiConfirmResponse;

public class BoiConfirmJSON implements IMessageProtocol {

	private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
			BoiConfirmJSON.class);

	public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
			throws ServerException {
		try {
			JSONObject jsonData = (JSONObject) aEncodedObj;
			BoiConfirmRequest an = (BoiConfirmRequest) aDecodingObj;
			try {
				String v = jsonData.getString("v");
				String[] arr = v.split(AIOConstants.SEPERATOR_BYTE_1);
				an.boiCode = Integer.parseInt(arr[0]);
				an.code = arr[1];
				return true;
			} catch (Exception ex) {
				mLog.error(ex.getMessage(), ex);
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public Object encode(IResponseMessage aResponseMessage)
			throws ServerException {
		try {
			JSONObject encodingObj = new JSONObject();
			BoiConfirmResponse boiC = (BoiConfirmResponse) aResponseMessage;
			/*if(boiC.session != null && boiC.session.getByteProtocol()> AIOConstants.PROTOCOL_ADVERTISING)
            {*/
                StringBuilder sb = new StringBuilder();
                sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Integer.toString(boiC.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
                if (boiC.mCode == ResponseCode.FAILURE) {
                	 sb.append(boiC.errMsg);
                }else {
                	for (Couple<String, String> aCouple : boiC.detail) {
    					sb.append(aCouple.e1).append(
    							AIOConstants.SEPERATOR_BYTE_1);
    					sb.append(aCouple.e2).append(
    							AIOConstants.SEPERATOR_BYTE_2);
    				}
    				if (sb.length() > 0)
    					sb.deleteCharAt(sb.length() - 1);
                }
                encodingObj.put("v", sb.toString());
                //return encodingObj;
            //}
			/*encodingObj.put("mid", aResponseMessage.getID());
			
			encodingObj.put("code", boiC.mCode);
			if (boiC.mCode == ResponseCode.SUCCESS) {
				StringBuilder sb = new StringBuilder();
				for (Couple<String, String> aCouple : boiC.detail) {
					sb.append(aCouple.e1).append(
							AIOConstants.SEPERATOR_BYTE_1);
					sb.append(aCouple.e2).append(
							AIOConstants.SEPERATOR_BYTE_2);
				}
				if (sb.length() > 0)
					sb.deleteCharAt(sb.length() - 1);

				encodingObj.put("v", sb.toString());
				return encodingObj;
			} else {
				encodingObj.put("error", boiC.errMsg);
			}*/
			return encodingObj;
		} catch (Throwable t) {
			mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
			return null;
		}
	}
}
