/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import allinone.protocol.messages.VivuDisappearResponse;

/**
 *
 * @author Vostro 3450
 */
public class VivuDisappearJSON implements IMessageProtocol {

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
            
            VivuDisappearResponse chat = (VivuDisappearResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
			sb.append(Integer.toString(aResponseMessage.getID())).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(Integer.toString(chat.mCode)).append(
					AIOConstants.SEPERATOR_NEW_MID);
			if (chat.mCode == ResponseCode.FAILURE) {
				sb.append(chat.errMgs);
			} else {
				sb.append(chat.uid);
			}
			encodingObj.put("v", sb.toString());
			return encodingObj;
			
            /*encodingObj.put("mid", aResponseMessage.getID());
            encodingObj.put("code", chat.mCode);
            if (chat.mCode == ResponseCode.FAILURE) {
                encodingObj.put("error_msg", chat.errMgs);
            } else if (chat.mCode == ResponseCode.SUCCESS) {
                encodingObj.put("uid", chat.uid);
            }
            return encodingObj;*/
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
