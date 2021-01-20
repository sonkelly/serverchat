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
import allinone.protocol.messages.GetRichestsResponse;

public class GetRichestsJSON implements IMessageProtocol {

    private final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetRichestsJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
            return true;
    }
    private String finalProtocol(GetRichestsResponse getFrientList) {
		StringBuilder sb = new StringBuilder();
		for (UserEntity userEntity : getFrientList.mRichestsList) {
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
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(userEntity.isLogin).append(
					AIOConstants.SEPERATOR_BYTE_2);
		}
		if (sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            
            GetRichestsResponse getRichestsList = (GetRichestsResponse) aResponseMessage;
            if(getRichestsList.session != null && getRichestsList.session.getByteProtocol()> AIOConstants.PROTOCOL_ADVERTISING)
            {
                StringBuilder sb = new StringBuilder();
                sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Integer.toString(getRichestsList.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
                if (getRichestsList.mCode == ResponseCode.FAILURE) {
                	 sb.append(getRichestsList.mErrorMsg);
                }else {
                	sb.append(finalProtocol(getRichestsList));
                }
                encodingObj.put("v", sb.toString());
                return encodingObj;
            }
            encodingObj.put("mid", aResponseMessage.getID());
            encodingObj.put("code", getRichestsList.mCode);
            if (getRichestsList.mCode == ResponseCode.FAILURE) {
            } else if (getRichestsList.mCode == ResponseCode.SUCCESS) {
                JSONArray arrRooms = new JSONArray();
                if (getRichestsList.mRichestsList != null) {
                    for (UserEntity userEntity : getRichestsList.mRichestsList) {
                        // with each playing room
                        JSONObject jRoom = new JSONObject();
                        // attached object
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
            // response encoded obj
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
