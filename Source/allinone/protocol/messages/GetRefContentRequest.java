package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;

public class GetRefContentRequest extends AbstractRequestMessage
{
  public GetRefContentRequest() {}
  
  public vn.game.protocol.IRequestMessage createNew() {
    return new GetRefContentRequest();
  }
}
