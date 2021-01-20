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
import allinone.protocol.messages.InviteRequest;
import allinone.protocol.messages.InviteResponse;

public class InviteJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(InviteJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            InviteRequest invite = (InviteRequest) aDecodingObj;
        	String s = jsonData.getString("v");
        	String[] arr = s.split(AIOConstants.SEPERATOR_BYTE_1);
        	invite.roomID = Long.parseLong(arr[0]);
        	invite.destUid = Long.parseLong(arr[1]);
            return true;
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();           
            InviteResponse invite = (InviteResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(invite.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (invite.mCode == ResponseCode.FAILURE) {
            	 sb.append(invite.mErrorMsg);
            }else {
            	sb.append(invite.sourceID).append(AIOConstants.SEPERATOR_BYTE_1);
            	sb.append(invite.roomID).append(AIOConstants.SEPERATOR_BYTE_1);
            	sb.append(invite.roomName).append(AIOConstants.SEPERATOR_BYTE_1);
            	sb.append(invite.sourceUserName).append(AIOConstants.SEPERATOR_BYTE_1);
            	sb.append(invite.minBet).append(AIOConstants.SEPERATOR_BYTE_1);
            	sb.append(invite.level).append(AIOConstants.SEPERATOR_BYTE_1);
            	sb.append(invite.currentZone).append(AIOConstants.SEPERATOR_BYTE_1);
            	sb.append(invite.timeout).append(AIOConstants.SEPERATOR_BYTE_1);
            	sb.append(invite.phongId);
            }
            encodingObj.put("v", sb.toString());
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
