/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages.json;

import org.json.JSONArray;
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
import allinone.data.UserEntity;
import allinone.protocol.messages.TopBetEuroPlayerResponse;

/**
 * 
 * @author mcb
 */
public class TopBetEuroPlayerJSON implements IMessageProtocol {

	private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
			TopBetEuroPlayerJSON.class);

	public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
			throws ServerException {
		return true;
	}

	public Object encode(IResponseMessage aResponseMessage)
			throws ServerException {
		try {
			JSONObject encodingObj = new JSONObject();

			TopBetEuroPlayerResponse matchRes = (TopBetEuroPlayerResponse) aResponseMessage;
			if (matchRes.session != null
					&& matchRes.session.getByteProtocol() > AIOConstants.PROTOCOL_ADVERTISING) {
				StringBuilder sb = new StringBuilder();
				sb.append(Integer.toString(aResponseMessage.getID())).append(
						AIOConstants.SEPERATOR_BYTE_1);
				sb.append(Integer.toString(matchRes.mCode)).append(
						AIOConstants.SEPERATOR_NEW_MID);
				if (matchRes.mCode == ResponseCode.SUCCESS) {
					StringBuilder sb1 = new StringBuilder();
					for (int i = 0; i < matchRes.users.size(); i++) {
						UserEntity entity = matchRes.users.get(i);
						sb1.append(entity.mUsername).append(
								AIOConstants.SEPERATOR_BYTE_1);
						sb1.append(entity.point).append(
								AIOConstants.SEPERATOR_BYTE_2);
					}
					if(sb1.length()>0) sb1.deleteCharAt(sb1.length()-1);
					sb.append(sb1);
				}
				encodingObj.put("v", sb.toString());
				return encodingObj;
			}

			encodingObj.put("code", ResponseCode.SUCCESS);
			encodingObj.put("mid", aResponseMessage.getID());
			JSONArray usersJarr = new JSONArray();
			int matchSize = matchRes.users.size();
			for (int i = 0; i < matchSize; i++) {
				UserEntity entity = matchRes.users.get(i);

				JSONObject jMatch = new JSONObject();

				jMatch.put("name", entity.mUsername);
				jMatch.put("point", entity.point);

				usersJarr.put(jMatch);
			}

			encodingObj.put("users", usersJarr);

			return encodingObj;
		} catch (JSONException ex) {
			mLog.error(ex.getMessage(), ex);
		}

		return null;
	}
}