package allinone.protocol.messages.json;



//import allinone.protocol.messages.KeepConnectionResponse;
import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.MessagesID;
import allinone.protocol.messages.KeepConnectionResponse;

public class KeepConnectionJSON implements IMessageProtocol
{
    
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException
    {
            return true;
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException
    {
         try {
            
            JSONObject encodingObj = new JSONObject();
            KeepConnectionResponse res = (KeepConnectionResponse)aResponseMessage;
//            if(res.session != null && res.session.getByteProtocol()> AIOConstants.PROTOCOL_ADVERTISING)
//            {
                StringBuilder sb = new StringBuilder();
                sb.append(Integer.toString(MessagesID.KEEP_CONNECTION)).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append("1").append(AIOConstants.SEPERATOR_NEW_MID);
                encodingObj.put("v", sb.toString());
                return encodingObj;
//            }
//             
//             encodingObj.put("mid", aResponseMessage.getID());
//            encodingObj.put("code", 1);
//            return encodingObj;
            
        } catch (Throwable t) {
            return null;
        }
    }	
}
