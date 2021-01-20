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
import allinone.protocol.messages.DHBCSureRequest;
import allinone.protocol.messages.DHBCSureResponse;

public class DHBCSureJSON implements IMessageProtocol {

	private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
			DHBCSureJSON.class);

	public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
			throws ServerException {
		 try {
	            JSONObject jsonData = (JSONObject) aEncodedObj;
	            DHBCSureRequest chat = (DHBCSureRequest) aDecodingObj;
	            chat.mMatchId = jsonData.getInt("v");
	            return true;
	        } catch (Throwable t) {
	            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
	            return false;
	        }
	}

	public Object encode(IResponseMessage aResponseMessage)
			throws ServerException {
		try {
			JSONObject encodingObj = new JSONObject();

			DHBCSureResponse response = (DHBCSureResponse) aResponseMessage;
			StringBuilder sb = new StringBuilder();
			sb.append(Integer.toString(aResponseMessage.getID())).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(Integer.toString(response.mCode)).append(
					AIOConstants.SEPERATOR_NEW_MID);
			if (response.mCode == ResponseCode.FAILURE) {
				sb.append(response.mErrorMsg);
			}
			encodingObj.put("v", sb.toString());
			return encodingObj;
		} catch (Throwable t) {
			mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
			return null;
		}
	}
}
