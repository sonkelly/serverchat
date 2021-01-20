package allinone.protocol.messages.json;

import java.util.Enumeration;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;
import allinone.protocol.messages.GetRoomMoneyResponse;

public class GetRoomMoneyJSON implements IMessageProtocol {

	private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
			GetRoomMoneyJSON.class);

	public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
			throws ServerException {
		return true;
	}

	public Object encode(IResponseMessage aResponseMessage)
			throws ServerException {
		try {
			JSONObject encodingObj = new JSONObject();
			encodingObj.put("mid", aResponseMessage.getID());
			GetRoomMoneyResponse getRoomMoney = (GetRoomMoneyResponse) aResponseMessage;
			encodingObj.put("code", getRoomMoney.mCode);
			if (getRoomMoney.mCode == ResponseCode.FAILURE) {
				encodingObj.put("error_msg", getRoomMoney.mErrorMsg);
			} else if (getRoomMoney.mCode == ResponseCode.SUCCESS) {
				JSONArray arrRooms = new JSONArray();
				if (getRoomMoney.moneys != null) {
					Enumeration<Integer> keys = getRoomMoney.moneys.keys();
					while (keys.hasMoreElements()) {
						int k = keys.nextElement();
						long v = getRoomMoney.moneys.get(k);
						JSONObject jRoom = new JSONObject();
						jRoom.put("room_type", k);
						jRoom.put("money", v);
						arrRooms.put(jRoom);
					}
				}
				encodingObj.put("room_money_list", arrRooms);
			}
			return encodingObj;
		} catch (Throwable t) {
			mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
			return null;
		}
	}
}
