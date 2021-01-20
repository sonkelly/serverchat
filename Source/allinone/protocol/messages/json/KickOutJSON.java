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
import allinone.protocol.messages.KickOutRequest;
import allinone.protocol.messages.KickOutResponse;

public class KickOutJSON implements IMessageProtocol {


    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            KickOutRequest kickOut = (KickOutRequest) aDecodingObj;
            if(jsonData.has("v")){
            	String s = jsonData.getString("v");
            	String[] arr = s.split(AIOConstants.SEPERATOR_BYTE_1);
            	kickOut.mMatchId = Long.parseLong(arr[0]);
                kickOut.uid = Long.parseLong(arr[1]);
            	return true;
            }
            kickOut.mMatchId = jsonData.getLong("match_id");
            kickOut.uid = jsonData.getLong("uid");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
           
            KickOutResponse kickOut = (KickOutResponse) aResponseMessage;
            if (kickOut.session != null
					&& kickOut.session.getByteProtocol() > AIOConstants.PROTOCOL_ADVERTISING) {
            	StringBuilder sb = new StringBuilder();
                sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Integer.toString(kickOut.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
                if (kickOut.mCode == ResponseCode.FAILURE) {
                	 sb.append(kickOut.message);
                }
                encodingObj.put("v", sb.toString());
				return encodingObj;
			}
            encodingObj.put("mid", aResponseMessage.getID());
            encodingObj.put("code", kickOut.mCode);
            if (kickOut.mCode == ResponseCode.FAILURE) {
                encodingObj.put("error_msg", kickOut.message);
            } else if (kickOut.mCode == ResponseCode.SUCCESS) {
            }
            return encodingObj;
        } catch (Throwable t) {
            return null;
        }
    }
}
