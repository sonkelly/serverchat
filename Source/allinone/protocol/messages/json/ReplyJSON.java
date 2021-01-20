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
import allinone.protocol.messages.ReplyRequest;
import allinone.protocol.messages.ReplyResponse;

/**
 *
 * @author binh_lethanh
 */
public class ReplyJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(ReplyJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            ReplyRequest matchReply = (ReplyRequest) aDecodingObj;
            String[] arr = jsonData.getString("v").split(AIOConstants.SEPERATOR_BYTE_1);
            matchReply.mMatchId = Long.parseLong(arr[0]);
            matchReply.mIsAccept = arr[1].equals("1");
            matchReply.buddy_uid = Long.parseLong(arr[2]);
            try {
            	 matchReply.directKey = Integer.valueOf(arr[3]);
            } catch (Exception ex) {
            	 matchReply.directKey = 0;
            }
            return true;
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            ReplyResponse matchReply = (ReplyResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(matchReply.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (matchReply.mCode == ResponseCode.FAILURE) {
            	 sb.append(matchReply.mErrorMsg);
            }
            
            encodingObj.put("v", sb.toString());
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
