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
import allinone.protocol.messages.InviteMXHRequest;
import allinone.protocol.messages.InviteMXHResponse;

public class InviteMXHJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(InviteMXHJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            InviteMXHRequest invite = (InviteMXHRequest) aDecodingObj;
            
            String s = jsonData.getString("v");
            String[] arr = s.split(AIOConstants.SEPERATOR_BYTE_1);
            invite.gameId = Integer.parseInt(arr[0]);
            invite.betMoney = Long.parseLong(arr[1]);
            invite.destUid = Long.parseLong(arr[2]);
            return true;
            
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
           
            InviteMXHResponse invite = (InviteMXHResponse) aResponseMessage;
           
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(invite.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (invite.mCode == ResponseCode.FAILURE) {
                sb.append(invite.mErrorMsg);
            }
            else
            {
                if(invite.value != null)
                    sb.append(invite.value);
            }
            
            encodingObj.put("v", sb.toString());
            return encodingObj;
            
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
