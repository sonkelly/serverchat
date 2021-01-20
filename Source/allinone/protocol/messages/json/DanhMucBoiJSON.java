package allinone.protocol.messages.json;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.Couple;
import allinone.data.ResponseCode;
import allinone.protocol.messages.DanhMucBoiRequest;
import allinone.protocol.messages.DanhMucBoiResponse;

public class DanhMucBoiJSON implements IMessageProtocol {

	private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
			DanhMucBoiJSON.class);

	public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
			throws ServerException {
		try {
			JSONObject jsonData = (JSONObject) aEncodedObj;
			DanhMucBoiRequest an = (DanhMucBoiRequest) aDecodingObj;
			try {
				
					String v = jsonData.getString("v");
                                        if(v.equals(""))
                                        {
                                            an.code = 0;
                                        }
                                        else
                                        {
					  an.code = Integer.parseInt(v);
                                        }
				
				return true;
			} catch (Exception ex) {
				mLog.error(ex.getMessage(), ex);
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public Object encode(IResponseMessage aResponseMessage)
			throws ServerException {
		try {
			JSONObject encodingObj = new JSONObject();
			DanhMucBoiResponse an = (DanhMucBoiResponse) aResponseMessage;
			StringBuilder sb = new StringBuilder();
			sb.append(Integer.toString(aResponseMessage.getID())).append(
					AIOConstants.SEPERATOR_BYTE_1);
			sb.append(Integer.toString(an.mCode)).append(
					AIOConstants.SEPERATOR_NEW_MID);
			if (an.mCode == ResponseCode.FAILURE) {
				sb.append(an.errMsg);
			} else {
				for (Couple<Integer, String> aCouple : an.danhMuc) {
					sb.append(aCouple.e1).append(
							AIOConstants.SEPERATOR_BYTE_1);
					sb.append(aCouple.e2).append(
							AIOConstants.SEPERATOR_BYTE_2);
				}
				if (sb.length() > 0)
					sb.deleteCharAt(sb.length() - 1);
			}
			encodingObj.put("v", sb.toString());
			return encodingObj;
		} catch (Throwable t) {
			mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
			return null;
		}
	}
}
