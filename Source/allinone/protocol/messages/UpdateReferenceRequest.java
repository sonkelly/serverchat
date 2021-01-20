package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class UpdateReferenceRequest extends AbstractRequestMessage
{
  public String refrenceCode;
  
  public UpdateReferenceRequest() {}
  
  public IRequestMessage createNew()
  {
    return new UpdateReferenceRequest();
  }
  
  public UpdateReferenceRequest(String uidRef) {
    refrenceCode = uidRef;
  }
}
