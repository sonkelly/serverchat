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
import allinone.protocol.messages.OutResponse;

public class OutJSON implements IMessageProtocol {

	private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
			OutJSON.class);

	public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
			throws ServerException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Object encode(IResponseMessage aResponseMessage)
			throws ServerException {
		try {
			
			JSONObject encodingObj = new JSONObject();

			OutResponse matchOut = (OutResponse) aResponseMessage;
			StringBuilder sb = new StringBuilder();
			
			sb.append(Integer.toString(aResponseMessage.getID())).append(
					AIOConstants.SEPERATOR_BYTE_1);
			
			sb.append(Integer.toString(matchOut.mCode)).append(
					AIOConstants.SEPERATOR_NEW_MID);
			
			if (matchOut.mCode == ResponseCode.FAILURE) {
				sb.append(matchOut.message);
			} else {
				sb.append(matchOut.mUid).append(
						AIOConstants.SEPERATOR_BYTE_1);
				sb.append(matchOut.username).append(
						AIOConstants.SEPERATOR_BYTE_1);
				sb.append(matchOut.message).append(
						AIOConstants.SEPERATOR_BYTE_1);
				sb.append(matchOut.out).append(
						AIOConstants.SEPERATOR_BYTE_1);
				sb.append(matchOut.newRoomOwner > 0 ? matchOut.newRoomOwner
								: "0").append(
						AIOConstants.SEPERATOR_BYTE_1);
				sb.append(matchOut.type);
			}
			
			encodingObj.put("v", sb.toString());

			// response encoded obj
			return encodingObj;
		} catch (Throwable t) {
			mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
			return null;
		}
	}
}
