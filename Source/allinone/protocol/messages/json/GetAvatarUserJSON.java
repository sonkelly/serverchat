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
import allinone.protocol.messages.GetAvatarUserResponse;

/**
 *
 * @author Vostro 3450
 */
public class GetAvatarUserJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(GetAvatarUserJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        return true;
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            GetAvatarUserResponse buy = (GetAvatarUserResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(buy.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (buy.mCode == ResponseCode.FAILURE) {
                sb.append(buy.mErrorMsg);
            } else {
                sb.append(buy.value);
            }
            encodingObj.put("v", sb.toString());
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
