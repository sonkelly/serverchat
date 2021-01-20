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
import allinone.protocol.messages.VivuGetGroupListResponse;

import com.allinone.vivu.Triple;

/**
 *
 * @author Vostro 3450
 */
public class VivuGetGroupListJSON implements IMessageProtocol {

    private final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(VivuChatJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        return true;
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            
            VivuGetGroupListResponse chat =
                    (VivuGetGroupListResponse) aResponseMessage;
            
            StringBuilder sb = new StringBuilder();
			sb.append(Integer.toString(aResponseMessage.getID())).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(Integer.toString(chat.mCode)).append(
					AIOConstants.SEPERATOR_NEW_MID);
			if (chat.mCode == ResponseCode.FAILURE) {
				sb.append(chat.errMgs);
			} else {
				for (Triple<Integer, Integer, Integer> d : chat.data) {
					sb.append(d.e1).append(AIOConstants.SEPERATOR_BYTE_1);//id_group
					sb.append(d.e2).append(AIOConstants.SEPERATOR_BYTE_1);// avaiable
					sb.append(d.e3).append(AIOConstants.SEPERATOR_BYTE_2);//maximum
				}
				sb.deleteCharAt(sb.length()-1);
			}
			encodingObj.put("v", sb.toString());
			return encodingObj;
			
			
            /*encodingObj.put("mid", aResponseMessage.getID());
            encodingObj.put("code", chat.mCode);
            if (chat.mCode == ResponseCode.FAILURE) {
                encodingObj.put("error_msg", chat.errMgs);
            } else if (chat.mCode == ResponseCode.SUCCESS) {
                JSONArray gs = new JSONArray();
                for (Triple d : chat.data) {
                    JSONObject g = new JSONObject();
                    g.put("id", d.e1);
                    g.put("avai", d.e2);
                    g.put("max", d.e3);
                    gs.put(g);
                }
                encodingObj.put("list", gs);
            }
            return encodingObj;*/
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
