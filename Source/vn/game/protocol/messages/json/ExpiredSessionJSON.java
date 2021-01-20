package vn.game.protocol.messages.json;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import vn.game.protocol.messages.ExpiredSessionResponse;
import allinone.data.AIOConstants;

public class ExpiredSessionJSON
  implements IMessageProtocol
{
  private final Logger mLog;

  public ExpiredSessionJSON()
  {
    this.mLog = LoggerContext.getLoggerFactory().getLogger(ExpiredSessionJSON.class);
  }

  public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Object encode(IResponseMessage aResponseMessage)
   throws ServerException {
      JSONObject encodingObj;
      try {
       encodingObj = new JSONObject();

       ExpiredSessionResponse login = (ExpiredSessionResponse) aResponseMessage;
       StringBuilder sb = new StringBuilder();
       sb.append(Integer.toString(aResponseMessage.getID())).append(
         AIOConstants.SEPERATOR_BYTE_1);
       sb.append("0").append(
         AIOConstants.SEPERATOR_NEW_MID);
       sb.append(login.mErrorMsg);

       encodingObj.put("v", sb.toString());
       
       return encodingObj;
       
      } catch (Throwable t) {
       this.mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
       return null;
  }
 }
}