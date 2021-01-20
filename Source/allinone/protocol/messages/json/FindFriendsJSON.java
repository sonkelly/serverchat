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
import allinone.protocol.messages.FindFriendsRequest;
import allinone.protocol.messages.FindFriendsResponse;

/**
 *
 * @author Dinhpv
 */
public class FindFriendsJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(FindFriendsJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            FindFriendsRequest findFriends = (FindFriendsRequest) aDecodingObj;
            
            	String s = jsonData.getString("v");
            	String[] arr = s.split(AIOConstants.SEPERATOR_BYTE_1);
//                if(arr.length>2)
//                {
 
                    findFriends.isMale = arr[0].equals("1");
                    findFriends.name = arr[1];
                    findFriends.countryId = Integer.parseInt(arr[2]);
                    findFriends.cityId = Integer.parseInt(arr[3]);
                    findFriends.pageIndex = Integer.parseInt(arr[4]);
                    
                   
//                }
//                else
//                {
//                    findFriends.pageIndex = Integer.parseInt(arr[0]);
//                    findFriends.requestId = Long.parseLong(arr[1]);
//                }

            	return true;
            
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
           
            FindFriendsResponse update = (FindFriendsResponse) aResponseMessage;
            
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(update.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (update.mCode == ResponseCode.FAILURE) {
                     sb.append(update.errMsg);
            }
            else
            {
                if(update.value != null)
                {
                    sb.append(update.value);
                }
            }
            encodingObj.put("v", sb.toString());
            return encodingObj;
            
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
