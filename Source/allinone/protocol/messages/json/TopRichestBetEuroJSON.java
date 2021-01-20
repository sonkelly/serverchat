/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages.json;

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
import allinone.data.Triple;
import allinone.protocol.messages.TopRichestBetEuroResponse;

/**
 * 
 * @author mcb
 */
public class TopRichestBetEuroJSON implements IMessageProtocol {

	private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
			TopRichestBetEuroJSON.class);

	public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
			throws ServerException {
		return true;
	}

	public Object encode(IResponseMessage aResponseMessage)
			throws ServerException {
		try {
			JSONObject encodingObj = new JSONObject();
			TopRichestBetEuroResponse matchRes = (TopRichestBetEuroResponse) aResponseMessage;

			StringBuilder sb = new StringBuilder();
			sb.append(Integer.toString(aResponseMessage.getID())).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(Integer.toString(matchRes.mCode)).append(
					AIOConstants.SEPERATOR_NEW_MID);
			if (matchRes.mCode == ResponseCode.SUCCESS) {
				for (int i = 0; i < matchRes.users.size(); i++) {
					Triple<String, Integer, Long> entity = matchRes.users.get(i);
					sb.append(entity.e1).append(
							AIOConstants.SEPERATOR_BYTE_1);
					sb.append(entity.e3).append(
							AIOConstants.SEPERATOR_BYTE_1);
					sb.append(entity.e2).append(
							AIOConstants.SEPERATOR_BYTE_2);
				}
				if (matchRes.users.size() > 0)
					sb.deleteCharAt(sb.length() - 1);
			}
			encodingObj.put("v", sb.toString());
			return encodingObj;

		} catch (JSONException ex) {
			mLog.error(ex.getMessage(), ex);
		}

		return null;
	}
}