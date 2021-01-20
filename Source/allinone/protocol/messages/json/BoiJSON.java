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
import allinone.protocol.messages.BoiRequest;
import allinone.protocol.messages.BoiResponse;

public class BoiJSON implements IMessageProtocol {

	private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
			BoiJSON.class);

	public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
			throws ServerException {
		try {
			JSONObject jsonData = (JSONObject) aEncodedObj;
			BoiRequest an = (BoiRequest) aDecodingObj;
			try {
				String v = jsonData.getString("v");
				String[] arr = v.split(AIOConstants.SEPERATOR_BYTE_1);
				an.code = Integer.parseInt(arr[0]);
				if(arr.length >=2) {
					an.data[0] = arr[1];
					if(arr.length>=3) {
						an.data[1] = arr[2];
						if(arr.length>=4) {
							an.data[2] = arr[3];
							if(arr.length>=5) {
								an.data[3] = arr[4];
							}
						}
					}
				}
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
			BoiResponse boiC = (BoiResponse) aResponseMessage;
			StringBuilder sb = new StringBuilder();
			 sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
             sb.append(Integer.toString(boiC.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
             if (boiC.mCode == ResponseCode.FAILURE) {
             	 sb.append(boiC.errMsg);
             }else {
            	sb.append(boiC.detail);
				
			}
             encodingObj.put("v", sb.toString());
			return encodingObj;
		} catch (Throwable t) {
			mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
			return null;
		}
	}
}
