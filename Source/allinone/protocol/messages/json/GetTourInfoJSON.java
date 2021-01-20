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
import allinone.protocol.messages.GetTourInfoRequest;
import allinone.protocol.messages.GetTourInfoResponse;

public class GetTourInfoJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(GetTourInfoJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            GetTourInfoRequest boc = (GetTourInfoRequest) aDecodingObj;
            boc.tourID = jsonData.getInt("v");            
            return true;
        } catch (Exception e) {
        	mLog.debug(e.getMessage());
            return false;
        }
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            GetTourInfoResponse boc = (GetTourInfoResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(boc.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (boc.mCode == ResponseCode.FAILURE) {
            	 sb.append(boc.eRRMess);
            }else {
            		sb.append(boc.mess);
            }
            encodingObj.put("v", sb.toString());
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
