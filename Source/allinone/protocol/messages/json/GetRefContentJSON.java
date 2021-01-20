package allinone.protocol.messages.json;

import allinone.data.AIOConstants;
import allinone.protocol.messages.GetRefContentResponse;
import java.io.PrintStream;
import org.json.JSONObject;
import org.slf4j.Logger;
import vn.game.common.ILoggerFactory;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;

public class GetRefContentJSON implements IMessageProtocol
{
  private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(GetRefContentJSON.class);
  
  public GetRefContentJSON() {}
  
  public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
    return true;
  }
  


  public Object encode(IResponseMessage aResponseMessage)
    throws ServerException
  {
    try
    {
      JSONObject encodingObj = new JSONObject();
      GetRefContentResponse getRef = (GetRefContentResponse)aResponseMessage;
      StringBuilder sb = new StringBuilder();
      sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
      sb.append(Integer.toString(getRef.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
      if (getRef.mCode == 0) {
        sb.append(getRef.mErrorMsg);
      } else {
        sb.append(getRef._smsContent).append(AIOConstants.SEPERATOR_BYTE_1);
        sb.append(getRef._dialogueContent).append(AIOConstants.SEPERATOR_BYTE_1);
        sb.append(getRef._referenceCode).append(AIOConstants.SEPERATOR_BYTE_1);
        sb.append(getRef._urlReference);
      }
      encodingObj.put("v", sb.toString());
      System.out.println("GetRefContentJSON encode : " + sb.toString());
      return encodingObj;
    } catch (Throwable t) {
      mLog.error("[ENCODER] " + aResponseMessage.getID(), t); }
    return null;
  }
}
