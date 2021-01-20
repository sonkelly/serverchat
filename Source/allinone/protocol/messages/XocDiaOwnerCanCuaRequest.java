package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class XocDiaOwnerCanCuaRequest
  extends AbstractRequestMessage
{
  public int betSide;
  
  @Override
  public IRequestMessage createNew()
  {
    return new XocDiaOwnerCanCuaRequest();
  }
}
