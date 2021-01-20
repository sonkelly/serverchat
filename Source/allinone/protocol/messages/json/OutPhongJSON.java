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
import allinone.protocol.messages.OutPhongResponse;

public class OutPhongJSON implements IMessageProtocol {

	private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
			OutPhongJSON.class);

	public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
			throws ServerException {
		return true;
	}

	public Object encode(IResponseMessage aResponseMessage)
			throws ServerException {
		try {
			JSONObject encodingObj = new JSONObject();
			
			OutPhongResponse out = (OutPhongResponse) aResponseMessage;
			
			 if(out.session != null && out.session.getByteProtocol()> AIOConstants.PROTOCOL_ADVERTISING)
	            {
	                StringBuilder sb = new StringBuilder();
	                sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
	                sb.append(Integer.toString(out.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
	                if (out.mCode == ResponseCode.FAILURE) {
	                	 sb.append(out.mErrorMsg);
	                }
	                encodingObj.put("v", sb.toString());
	                return encodingObj;
	            }
			encodingObj.put("code", out.mCode);
			encodingObj.put("mid", aResponseMessage.getID());
			if (out.mCode == ResponseCode.FAILURE) {
				encodingObj.put("error_msg", out.mErrorMsg);
			}
			return encodingObj;
		} catch (Throwable t) {
			mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
			return null;
		}
	}
}
