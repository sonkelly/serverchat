/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages.json;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.ResponseCode;
import allinone.data.ZoneID;
import allinone.protocol.messages.HamVuiRequest;
import allinone.protocol.messages.HamVuiResponse;

/**
 * 
 * @author binh_lethanh
 */
public class HamVuiJSON implements IMessageProtocol {

	private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
			HamVuiJSON.class);

	public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
			throws ServerException {
		try {
			JSONObject jsonData = (JSONObject) aEncodedObj;
			HamVuiRequest reconnectRq = (HamVuiRequest) aDecodingObj;
			reconnectRq.username = jsonData.getString("u");
			// reconnectRq.uid = jsonData.getLong("uid");
			if (jsonData.has("v")) {
				try {
					String v = jsonData.getString("v");
					String[] arrValues = v
							.split(AIOConstants.STRING_SEPERATOR_ELEMENT);
					int type = Integer.parseInt(arrValues[0]);
					reconnectRq.type = type;
					switch (type) {
					case 1:
					case 2:
					case 3:
						// reconnectRq.username = arrValues[1];
						reconnectRq.pass = arrValues[1];
						if (type == 2 || type == 3) {
							reconnectRq.zone = Integer.parseInt(arrValues[2]);
							if (type == 3) {
								reconnectRq.phong = Integer
										.parseInt(arrValues[3]);
							}
						}
						break;
					case 4:
					case 5:
						reconnectRq.uid = Long.parseLong(arrValues[1]);
						reconnectRq.matchId = Long.parseLong(arrValues[2]);
						if (type == 5)
							reconnectRq.tourID = Integer.parseInt(arrValues[3]);
						break;
					default:
						break;
					}
				} catch (Exception ex) {
					mLog.error(ex.getMessage(), ex);
				}
			}
			return true;
		} catch (Throwable t) {
			mLog.error("[DECODER] " + aDecodingObj.getID(), t);
			return false;
		}
	}

	

	private void newProtocol(HamVuiResponse matchJoin, JSONObject encodingObj)
			throws JSONException {
		StringBuilder sb = new StringBuilder();
		sb.append(matchJoin.minBet).append(AIOConstants.SEPERATOR_BYTE_1);
		/*
		 * sb.append(matchJoin.roomOwner.id).append(
		 * AIOConstants.SEPERATOR_BYTE_1);
		 */
		switch (matchJoin.zoneID) {
		case ZoneID.PHOM: {
			sb.append(matchJoin.isPlaying ? "1" : "0").append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(matchJoin.isAn ? "1" : "0").append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(matchJoin.isTaiGui ? "1" : "0");// .append(AIOConstants.SEPERATOR_BYTE_1);
			// if(matchJoin.isObserve){
			/*sb.append(AIOConstants.SEPERATOR_BYTE_1)
			.append(matchJoin.duty)
			.append(AIOConstants.SEPERATOR_BYTE_1);*/
			sb.append(AIOConstants.SEPERATOR_BYTE_1)
					.append(matchJoin.turn)
					.append(AIOConstants.SEPERATOR_BYTE_1);
			sb.append(matchJoin.deck)
					.append(AIOConstants.SEPERATOR_BYTE_1);
			sb.append(matchJoin.cards).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(matchJoin.currCard).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(matchJoin.mMatchId);
			// }
			sb.append(AIOConstants.SEPERATOR_BYTE_3);
			

			break;
		}
		default: {
			break;
		}
		}
		encodingObj.put("v", sb.toString());
		// return encodingObj;
	}

	public Object encode(IResponseMessage aResponseMessage)
			throws ServerException {
		try {
			JSONObject encodingObj = new JSONObject();
			encodingObj.put("mid", aResponseMessage.getID());
			HamVuiResponse reconnectRes = (HamVuiResponse) aResponseMessage;
			encodingObj.put("code", reconnectRes.mCode);
			if (reconnectRes.mCode == ResponseCode.FAILURE) {
				encodingObj.put("error_msg", reconnectRes.mErrorMsg);
			} else if (reconnectRes.mCode == ResponseCode.SUCCESS) {
				if (reconnectRes.isNeeded) {
					newProtocol(reconnectRes, encodingObj);
					return encodingObj;
				}
			}
			return encodingObj;
		} catch (Throwable t) {
			mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
			return null;
		}
	}
}
