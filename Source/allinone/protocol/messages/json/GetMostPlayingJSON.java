package allinone.protocol.messages.json;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.protocol.messages.GetMostPlayingResponse;

public class GetMostPlayingJSON implements IMessageProtocol {

    private final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetMostPlayingJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
            return true;
    }
    private String finalProtocol(GetMostPlayingResponse getFrientList) {
		StringBuilder sb = new StringBuilder();
		for (UserEntity userEntity : getFrientList.mMostPlayingist) {
			sb.append(userEntity.mUid).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(userEntity.mUsername).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(userEntity.avatarID).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(userEntity.level).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(userEntity.money).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(userEntity.playsNumber).append(
					AIOConstants.SEPERATOR_BYTE_2);
		}
		if (sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            GetMostPlayingResponse getFrientList = (GetMostPlayingResponse) aResponseMessage;
            if(getFrientList.session != null && getFrientList.session.getByteProtocol()> AIOConstants.PROTOCOL_ADVERTISING)
            {
                StringBuilder sb = new StringBuilder();
                sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Integer.toString(getFrientList.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
                if (getFrientList.mCode == ResponseCode.FAILURE) {
                	 sb.append(getFrientList.mErrorMsg);
                }else {
                	sb.append(finalProtocol(getFrientList));
                }
                encodingObj.put("v", sb.toString());
                return encodingObj;
            }
            encodingObj.put("mid", aResponseMessage.getID());
            encodingObj.put("code", getFrientList.mCode);
            if (getFrientList.mCode == ResponseCode.FAILURE) {
            } else if (getFrientList.mCode == ResponseCode.SUCCESS) {
                JSONArray arrRooms = new JSONArray();
                if (getFrientList.mMostPlayingist != null) {
                    for (UserEntity userEntity : getFrientList.mMostPlayingist) {
                        JSONObject jRoom = new JSONObject();
                        jRoom.put("username", userEntity.mUsername);
                        jRoom.put("uid", userEntity.mUid);
                        jRoom.put("avatar", userEntity.avatarID);
                        jRoom.put("level", userEntity.level);
                        jRoom.put("money", userEntity.money);
                        jRoom.put("PlaysNumber", userEntity.playsNumber);
                        arrRooms.put(jRoom);
                    }
                }
                encodingObj.put("frient_list", arrRooms);
            }
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
