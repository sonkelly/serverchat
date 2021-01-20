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
import allinone.protocol.messages.VivuChooseGroupRequest;
import allinone.protocol.messages.VivuChooseGroupResponse;

/**
 *
 * @author Vostro 3450
 */
public class VivuChooseGroupJSON implements IMessageProtocol {

    private final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(VivuChooseGroupJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            VivuChooseGroupRequest pChat = (VivuChooseGroupRequest) aDecodingObj;
            pChat.groupID = jsonData.getInt("v");
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
           
            VivuChooseGroupResponse chat = (VivuChooseGroupResponse) aResponseMessage;

            StringBuilder sb = new StringBuilder();
			sb.append(Integer.toString(aResponseMessage.getID())).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(Integer.toString(chat.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
			if (chat.mCode == ResponseCode.FAILURE) {
				sb.append(chat.errMgs);
			}
			encodingObj.put("v", sb.toString());
			return encodingObj;
			
            /*encodingObj.put("mid", aResponseMessage.getID());
            encodingObj.put("code", chat.mCode);
            if (chat.mCode == ResponseCode.FAILURE) {
                encodingObj.put("error_msg", chat.errMgs);
            }
            return encodingObj;*/
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
