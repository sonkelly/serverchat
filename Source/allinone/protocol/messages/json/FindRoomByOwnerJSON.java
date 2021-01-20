package allinone.protocol.messages.json;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import vn.game.room.RoomEntity;
import allinone.data.ResponseCode;
import allinone.protocol.messages.FindRoomByOwnerRequest;
import allinone.protocol.messages.FindRoomByOwnerResponse;

public class FindRoomByOwnerJSON implements IMessageProtocol {

	private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
			GetWaitingListJSON.class);

	public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
			throws ServerException {
		try {
			JSONObject jsonData = (JSONObject) aEncodedObj;
			FindRoomByOwnerRequest findRoom = (FindRoomByOwnerRequest) aDecodingObj;
			findRoom.roomOwner = jsonData.getString("room_owner");
			return true;
		} catch (Throwable t) {
			mLog.error("[DECODER] " + aDecodingObj.getID(), t);
			return false;
		}
	}

	public Object encode(IResponseMessage aResponseMessage)
			throws ServerException {
		try {
			JSONObject encodingObj = new JSONObject();
			encodingObj.put("mid", aResponseMessage.getID());
			FindRoomByOwnerResponse findRoom = (FindRoomByOwnerResponse) aResponseMessage;
			encodingObj.put("code", findRoom.mCode);
			if (findRoom.mCode == ResponseCode.FAILURE) {
				encodingObj.put("error_msg", findRoom.mErrorMsg);
			} else if (findRoom.mCode == ResponseCode.SUCCESS) {
				if (findRoom.mRoom != null) {
					RoomEntity roomEntity = findRoom.mRoom;

					encodingObj.put("room_id", roomEntity.mRoomId);
					encodingObj.put("room_name", roomEntity.mRoomName);
					encodingObj.put("playing_size", roomEntity.mPlayingSize);

					if (roomEntity.mPassword != null) {
						encodingObj.put("isSecure", true);
						encodingObj.put("password", roomEntity.mPassword);
					} else {
						encodingObj.put("isSecure", false);
					}
//					BacayTable table = (BacayTable) roomEntity.mAttactmentData;
//					if (table != null) {
//						encodingObj.put("capacity", table.getMaximumPlayer());
//						encodingObj.put("minBet", table.getMinBet());
//						BacayPlayer roomOwner = table.getRoomOwner();
//						encodingObj.put("level", roomOwner.level);
//						encodingObj.put("avatar", roomOwner.avatarID);
//						encodingObj.put("username", roomOwner.username);
//
//					}

				}
			}
			return encodingObj;
		} catch (Throwable t) {
			mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
			return null;
		}
	}
}
