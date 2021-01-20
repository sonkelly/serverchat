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
import allinone.protocol.messages.SuggestRequest;
import allinone.protocol.messages.SuggestResponse;

public class SuggestJSON implements IMessageProtocol {

	private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
			SuggestJSON.class);

	public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
			throws ServerException {
		try {
			JSONObject jsonData = (JSONObject) aEncodedObj;
			SuggestRequest matchStart = (SuggestRequest) aDecodingObj;
			if(jsonData.has("v")) {
				matchStart.note = jsonData.getString("v");
				return true;
			}
			matchStart.uid = jsonData.getLong("uid");
			matchStart.note = jsonData.getString("note");
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
			
			SuggestResponse suggest = (SuggestResponse) aResponseMessage;
			if(suggest.session != null && suggest.session.getByteProtocol()> AIOConstants.PROTOCOL_ADVERTISING)
            {
                StringBuilder sb = new StringBuilder();
                sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Integer.toString(suggest.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
                if (suggest.mCode == ResponseCode.FAILURE) {
                	 sb.append(suggest.mErrorMsg);
                }
                encodingObj.put("v", sb.toString());
                return encodingObj;
            }
			encodingObj.put("mid", aResponseMessage.getID());
			encodingObj.put("code", suggest.mCode);
			if (suggest.mCode == ResponseCode.FAILURE) {
				encodingObj.put("error_msg", suggest.mErrorMsg);
			}
			return encodingObj;
		} catch (Throwable t) {
			mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
			return null;
		}
	}
}
