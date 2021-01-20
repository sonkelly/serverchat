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
import allinone.protocol.messages.ChanAnChanRequest;
import allinone.protocol.messages.ChanAnChanResponse;

public class ChanAnChanJSON implements IMessageProtocol {

	private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
			ChanAnChanJSON.class);

	public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
			throws ServerException {
		try {
			JSONObject jsonData = (JSONObject) aEncodedObj;
			ChanAnChanRequest matchStart = (ChanAnChanRequest) aDecodingObj;
			matchStart.card = jsonData.getInt("v");
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

			ChanAnChanResponse matchStart = (ChanAnChanResponse) aResponseMessage;
			StringBuilder sb = new StringBuilder();
			sb.append(Integer.toString(aResponseMessage.getID())).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(Integer.toString(matchStart.mCode)).append(
					AIOConstants.SEPERATOR_NEW_MID);
			if (matchStart.mCode == ResponseCode.FAILURE) {
				sb.append(matchStart.mErrorMsg);
			} else {
				sb.append(matchStart.uid).append(
						AIOConstants.SEPERATOR_BYTE_1);
				sb.append(matchStart.card).append(
						AIOConstants.SEPERATOR_BYTE_1);
				sb.append(matchStart.errorCode);
			}
			encodingObj.put("v", sb.toString());
			return encodingObj;

		} catch (Throwable t) {
			mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
			return null;
		}
	}
}
