package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class XocDiaMuaRequest
  extends AbstractRequestMessage
{
  public long money;
  
  public IRequestMessage createNew()
  {
    return new XocDiaMuaRequest();
  }
}
