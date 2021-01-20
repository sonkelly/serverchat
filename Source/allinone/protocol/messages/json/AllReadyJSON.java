package allinone.protocol.messages.json;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.protocol.messages.AllReadyResponse;

public class AllReadyJSON implements IMessageProtocol
{

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(AllReadyJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException
    {
        try
        {
            JSONObject encodingObj = new JSONObject();
            // put response data into json object
            encodingObj.put("mid", aResponseMessage.getID());
            // cast response obj
            AllReadyResponse matchAllReady = (AllReadyResponse) aResponseMessage;
            encodingObj.put("code", matchAllReady.mCode);
            // response encoded obj
            return encodingObj;
        } catch (Throwable t)
        {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
