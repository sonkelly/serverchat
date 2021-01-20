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


import allinone.protocol.messages.NumberOnlineResponse;

public class NumberOnlineJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
            NumberOnlineJSON.class);

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
            NumberOnlineResponse resCommond = (NumberOnlineResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(
                    AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(resCommond.mCode)).append(
                    AIOConstants.SEPERATOR_NEW_MID);
            if (resCommond.mCode == ResponseCode.FAILURE) {
                sb.append(resCommond.errMgs);
            } else {
                sb.append(resCommond.number_online);
            }
            
            encodingObj.put("v", sb.toString());
            return encodingObj;

        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }

}
