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
import allinone.protocol.messages.GetOtherPokerResponse;

@SuppressWarnings("unused")
public class GetOtherPokerJSON implements IMessageProtocol {

	private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
			GetOtherPokerJSON.class);

	public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
			throws ServerException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Object encode(IResponseMessage aResponseMessage)
			throws ServerException {
		try {
			JSONObject encodingObj = new JSONObject();

			GetOtherPokerResponse getPoker = (GetOtherPokerResponse) aResponseMessage;

			StringBuilder sb = new StringBuilder();
			sb.append(Integer.toString(aResponseMessage.getID())).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(Integer.toString(getPoker.mCode)).append(
					AIOConstants.SEPERATOR_NEW_MID);
			if (getPoker.mCode == ResponseCode.SUCCESS) {
				sb.append(getPoker.uid).append(
						AIOConstants.SEPERATOR_BYTE_1);
				sb.append(getPoker.isNew).append(
						AIOConstants.SEPERATOR_BYTE_1);
				StringBuilder sb1 = new StringBuilder();
				
				if (sb1.length() > 0)
					sb1.deleteCharAt(sb1.length() - 1);
				sb.append(sb1);
			}
			encodingObj.put("v", sb.toString());
			return encodingObj;
		} catch (Throwable t) {
			mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
			return null;
		}
	}
}
