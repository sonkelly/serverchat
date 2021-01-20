package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class ViewNameRequest
  extends AbstractRequestMessage
{
  public String viewname;
  
  @Override
  public IRequestMessage createNew()
  {
    return new ViewNameRequest();
  }
}
