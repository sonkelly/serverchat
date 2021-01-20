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
import allinone.protocol.messages.UpdateStatusRequest;
import allinone.protocol.messages.UpdateStatusResponse;

/**
 * 
 * @author Vostro 3450
 */
public class UpdateStatusJSON implements IMessageProtocol {

	private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
			UpdateStatusJSON.class);

	@Override
	public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
			throws ServerException {
		try {
			JSONObject jsonData = (JSONObject) aEncodedObj;
			UpdateStatusRequest pChat = (UpdateStatusRequest) aDecodingObj;
			String s = jsonData.getString("v");
			String[] arr = s.split(AIOConstants.SEPERATOR_BYTE_1);
			pChat.status = Integer.parseInt(arr[0]);// jsonData.getInt("zone");
			pChat.buddy_uid = Long.parseLong(arr[1]);
			return true;
		} catch (Throwable t) {
			mLog.error("[DECODER] " + aDecodingObj.getID(), t);
			return false;
		}
	}

	@Override
	public Object encode(IResponseMessage aResponseMessage)
			throws ServerException {
		try {
			JSONObject encodingObj = new JSONObject();

			UpdateStatusResponse chat = (UpdateStatusResponse) aResponseMessage;
			StringBuilder sb = new StringBuilder();
			sb.append(Integer.toString(aResponseMessage.getID())).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(Integer.toString(chat.mCode)).append(
					AIOConstants.SEPERATOR_NEW_MID);
			if (chat.mCode == ResponseCode.FAILURE) {
				sb.append(chat.errMgs);
			} else {
				sb.append(chat.uid).append(AIOConstants.SEPERATOR_BYTE_1);
				sb.append(chat.status).append(AIOConstants.SEPERATOR_BYTE_1);
				sb.append(chat.d_uid);//.append(AIOConstants.SEPERATOR_BYTE_1);
			}
			encodingObj.put("v", sb.toString());
			return encodingObj;

			/*encodingObj.put("mid", aResponseMessage.getID());
			encodingObj.put("code", chat.mCode);
			if (chat.mCode == ResponseCode.FAILURE) {
				encodingObj.put("error_msg", chat.errMgs);
			} else if (chat.mCode == ResponseCode.SUCCESS) {
				encodingObj.put("uid", chat.uid);
				encodingObj.put("status", chat.status);
				encodingObj.put("d_uid", chat.d_uid);
			}
			return encodingObj;*/
		} catch (Throwable t) {
			mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
			return null;
		}
	}
}
