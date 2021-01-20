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
import allinone.protocol.messages.GetFriendRequest;
import allinone.protocol.messages.GetFriendResponse;

/**
 *
 * @author binh_lethanh
 */
public class GetFriendJSON implements IMessageProtocol {//check co phai la friend ko

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(GetFriendJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            GetFriendRequest matchReply = (GetFriendRequest) aDecodingObj;
            
            String[] arr = jsonData.getString("v").split(AIOConstants.SEPERATOR_BYTE_1);
            matchReply.userId = Integer.parseInt(arr[0]);
//            matchReply.destUid = Long.parseLong(arr[1]);
//            matchReply.isAccept = arr[2].equals("1");
            
            
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
            GetFriendResponse matchReply = (GetFriendResponse) aResponseMessage;
            
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(matchReply.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (matchReply.mCode == ResponseCode.FAILURE) {
                     sb.append(matchReply.mErrorMsg);
            }
            else
            {
                sb.append(matchReply.value);
            }

            encodingObj.put("v", sb.toString());
            return encodingObj;
            
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
