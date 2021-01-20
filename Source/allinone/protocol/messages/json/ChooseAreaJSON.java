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
import allinone.protocol.messages.ChooseAreaRequest;
import allinone.protocol.messages.ChooseAreaResponse;

import com.allinone.vivu.FuckingPlayer;

/**
 *
 * @author Vostro 3450
 */
public class ChooseAreaJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(ChooseCityJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            ChooseAreaRequest chat = (ChooseAreaRequest) aDecodingObj;
            String s = jsonData.getString("v");
            String[] arr = s.split(AIOConstants.SEPERATOR_BYTE_1);
            chat.zone = Integer.parseInt(arr[0]);//jsonData.getInt("zone");
            chat.x = Integer.parseInt(arr[1]);
            chat.y = Integer.parseInt(arr[2]);
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
            
            ChooseAreaResponse chat = (ChooseAreaResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(chat.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (chat.mCode == ResponseCode.FAILURE) {
            	 sb.append(chat.mErrorMsg);
            }else {
            	sb.append(chat.groupID).append(AIOConstants.SEPERATOR_BYTE_3);
            	for(FuckingPlayer u : chat.users){
            		sb.append(u.id).append(AIOConstants.SEPERATOR_BYTE_1);
            		sb.append(u.username).append(AIOConstants.SEPERATOR_BYTE_1);
            		sb.append(u.xPos).append(AIOConstants.SEPERATOR_BYTE_1);
            		sb.append(u.yPos).append(AIOConstants.SEPERATOR_BYTE_1);
            		sb.append(u.getAttr()).append(AIOConstants.SEPERATOR_BYTE_2);
                }
            	/*if(chat.users.size()>0) */sb.deleteCharAt(sb.length()-1);
            }
            encodingObj.put("v", sb.toString());
            return encodingObj;
            
            /*encodingObj.put("mid", aResponseMessage.getID());
            encodingObj.put("code", chat.mCode);
            if (chat.mCode == ResponseCode.FAILURE) {
                encodingObj.put("error_msg", chat.mErrorMsg);
            }else {
                JSONArray users = new JSONArray();
                for(FuckingPlayer u : chat.users){
                    JSONObject user = new JSONObject();
                    user.put("attr", u.getAttr());
                    user.put("x", u.xPos);
                    user.put("y", u.yPos);
                    user.put("uid", u.id);
                    user.put("name", u.username);
                    users.put(user);
                }
                encodingObj.put("users", users);
                encodingObj.put("group_id", chat.groupID);
            }
            return encodingObj;*/
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
