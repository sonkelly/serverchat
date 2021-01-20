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
import allinone.protocol.messages.ChanDanhChanRequest;
import allinone.protocol.messages.ChanDanhChanResponse;

public class ChanDanhChanJSON implements IMessageProtocol {

	private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
			ChanDanhChanJSON.class);

	public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
			throws ServerException {
		try {
			JSONObject jsonData = (JSONObject) aEncodedObj;
			ChanDanhChanRequest matchStart = (ChanDanhChanRequest) aDecodingObj;
			String str = jsonData.getString("v");
			String[] arr = str.split(AIOConstants.SEPERATOR_BYTE_1);
			matchStart.card = Integer.parseInt(arr[0]);
			if (arr.length > 1)
				matchStart.destID = Long.parseLong(arr[1]);
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
			ChanDanhChanResponse matchStart = (ChanDanhChanResponse) aResponseMessage;
			StringBuilder sb = new StringBuilder();
			sb.append(Integer.toString(aResponseMessage.getID())).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(Integer.toString(matchStart.mCode)).append(
					AIOConstants.SEPERATOR_NEW_MID);
			if (matchStart.mCode == ResponseCode.FAILURE) {
				sb.append(matchStart.errMess);
			} else {
				sb.append(matchStart.uid).append(
						AIOConstants.SEPERATOR_BYTE_1);
				sb.append(matchStart.card).append(
						AIOConstants.SEPERATOR_BYTE_1);
				sb.append(matchStart.nextID).append(
						AIOConstants.SEPERATOR_BYTE_1);
				sb.append(matchStart.chiuUid).append(AIOConstants.SEPERATOR_BYTE_1);
				sb.append(matchStart.destID).append(AIOConstants.SEPERATOR_BYTE_1);
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
