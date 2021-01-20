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
import allinone.protocol.messages.GetUserInfoRequest;
import allinone.protocol.messages.GetUserInfoResponse;

public class GetUserInfoJSON implements IMessageProtocol {

	private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
			GetUserInfoJSON.class);

	public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
			throws ServerException {
		try {
			JSONObject jsonData = (JSONObject) aEncodedObj;
			GetUserInfoRequest getUserInfo = (GetUserInfoRequest) aDecodingObj;
                        if(jsonData.has("v"))
		 	    getUserInfo.mUid = jsonData.getLong("v");
                        else
                            getUserInfo.mUid = jsonData.getLong("uid");
			return true;
		} catch (Throwable t) {
			mLog.error("[DECODER] " + aDecodingObj.getID(), t);
			return false;
		}
	}
	private String finalProtocol(GetUserInfoResponse getFrientList) {
		StringBuilder sb = new StringBuilder();
			sb.append(getFrientList.mUid).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(getFrientList.mUsername).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(getFrientList.AvatarID).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(getFrientList.level).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(getFrientList.money).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(getFrientList.playsNumber).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(getFrientList.experience).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(getFrientList.isFriend?1:0).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(getFrientList.mIsMale?1:0).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(getFrientList.mAge).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(getFrientList.vipName);
		return sb.toString();
	}
	public Object encode(IResponseMessage aResponseMessage)
			throws ServerException {
		try {
			JSONObject encodingObj = new JSONObject();
			
			GetUserInfoResponse getUserInfo = (GetUserInfoResponse) aResponseMessage;
			if(getUserInfo.session != null && getUserInfo.session.getByteProtocol()> AIOConstants.PROTOCOL_ADVERTISING)
                        {
                            StringBuilder sb = new StringBuilder();
                            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
                            sb.append(Integer.toString(getUserInfo.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
                            if (getUserInfo.mCode == ResponseCode.FAILURE) {
                                     sb.append(getUserInfo.mErrorMsg);
                            }else {
                                    sb.append(finalProtocol(getUserInfo));
                            }
                            encodingObj.put("v", sb.toString());
                            return encodingObj;
                        }
                        
                        
			encodingObj.put("mid", aResponseMessage.getID());
			encodingObj.put("code", getUserInfo.mCode);
			if (getUserInfo.mCode == ResponseCode.FAILURE) {
				encodingObj.put("error_msg", getUserInfo.mErrorMsg);
			} else if (getUserInfo.mCode == ResponseCode.SUCCESS) {
				encodingObj.put("uid", getUserInfo.mUid);
				encodingObj.put("username", getUserInfo.mUsername);
				encodingObj.put("age", getUserInfo.mAge);
				encodingObj.put("is_male", getUserInfo.mIsMale);
				encodingObj.put("avatar", getUserInfo.AvatarID);
				encodingObj.put("money", getUserInfo.money);
				encodingObj.put("level", getUserInfo.level);
				encodingObj.put("playsNumber", getUserInfo.playsNumber);
				encodingObj.put("is_friend", getUserInfo.isFriend);
				encodingObj.put("experience", getUserInfo.experience);
				encodingObj.put("vipName", getUserInfo.vipName);
			}
			return encodingObj;
		} catch (Throwable t) {
			mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
			return null;
		}
	}

}
