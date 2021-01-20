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
import allinone.data.HTTPPoster;
import allinone.data.ResponseCode;

import allinone.protocol.messages.CountEmailResponse;

public class CountEmailJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
            CountEmailJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
            throws ServerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage)
            throws ServerException {
        try {

            JSONObject encodingObj = new JSONObject();
            CountEmailResponse resCommond = (CountEmailResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(
                    AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(resCommond.mCode)).append(
                    AIOConstants.SEPERATOR_NEW_MID);
            if (resCommond.mCode == ResponseCode.FAILURE) {
                sb.append(resCommond.errMsg);
            } else {
                sb.append(resCommond.count).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(HTTPPoster.HOME_HTTP + HTTPPoster.API_EVENTS).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(HTTPPoster.HOME_HTTP + HTTPPoster.API_NEWS).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(resCommond.smsKH_VTT).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(resCommond.smsNum).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(resCommond.smsKH_VINA).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(resCommond.smsNum).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(resCommond.smsKH_MOBI).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(resCommond.smsNum).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(HTTPPoster.AVATAR_HTTP);
            }
            
            encodingObj.put("v", sb.toString());
            return encodingObj;

        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }

}
