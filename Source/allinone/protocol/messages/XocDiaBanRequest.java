package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class XocDiaBanRequest
  extends AbstractRequestMessage
{
  public int betSide;
  
  public IRequestMessage createNew()
  {
    return new XocDiaBanRequest();
  }
}
