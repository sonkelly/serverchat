package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class XocDiaDuoiCuaRequest
  extends AbstractRequestMessage
{
  public int betside;
  
  public IRequestMessage createNew()
  {
    return new XocDiaDuoiCuaRequest();
  }
}
