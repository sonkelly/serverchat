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
import allinone.protocol.messages.VivuChatRequest;
import allinone.protocol.messages.VivuChatResponse;

/**
 *
 * @author Vostro 3450
 */
public class VivuChatJSON implements IMessageProtocol {

    private final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(VivuChatJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            VivuChatRequest pChat = (VivuChatRequest) aDecodingObj;
            String s = jsonData.getString("v");
            String[] arr = s.split(AIOConstants.SEPERATOR_BYTE_1);
            pChat.mMessage = arr[1];
            pChat.type = Integer.parseInt(arr[0]);
            return true;
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
           
            VivuChatResponse chat = (VivuChatResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
			sb.append(Integer.toString(aResponseMessage.getID())).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(Integer.toString(chat.mCode)).append(
					AIOConstants.SEPERATOR_NEW_MID);
			if (chat.mCode == ResponseCode.FAILURE) {
				sb.append(chat.errMgs);
			} else {
				sb.append(chat.uid).append(AIOConstants.SEPERATOR_BYTE_1);
				sb.append(chat.mess);//.append(AIOConstants.SEPERATOR_BYTE_1);
			}
			encodingObj.put("v", sb.toString());
			return encodingObj;
			
            /*encodingObj.put("mid", aResponseMessage.getID());
            encodingObj.put("code", chat.mCode);
            if (chat.mCode == ResponseCode.FAILURE) {
                encodingObj.put("error_msg", chat.errMgs);
            } else if (chat.mCode == ResponseCode.SUCCESS) {
                encodingObj.put("uid", chat.uid);
                encodingObj.put("message", chat.mess);
            }
            return encodingObj;*/
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
