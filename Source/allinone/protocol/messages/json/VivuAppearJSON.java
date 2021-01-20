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
import allinone.protocol.messages.VivuAppearResponse;

public class VivuAppearJSON implements IMessageProtocol {

    private final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(VivuDisappearJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        return true;
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            
            VivuAppearResponse chat = (VivuAppearResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
			sb.append(Integer.toString(aResponseMessage.getID())).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(Integer.toString(chat.mCode)).append(
					AIOConstants.SEPERATOR_NEW_MID);
			if (chat.mCode == ResponseCode.FAILURE) {
				sb.append(chat.errMgs);
			} else {
				sb.append(chat.uid).append(
						AIOConstants.SEPERATOR_BYTE_1);
				sb.append(chat.name).append(
						AIOConstants.SEPERATOR_BYTE_1);
				sb.append(chat.xPos).append(
						AIOConstants.SEPERATOR_BYTE_1);
				sb.append(chat.yPos).append(
						AIOConstants.SEPERATOR_BYTE_1);
				sb.append(chat.attr);
			}
			encodingObj.put("v", sb.toString());
			return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
