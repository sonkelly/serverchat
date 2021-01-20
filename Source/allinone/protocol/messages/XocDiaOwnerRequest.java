package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class XocDiaOwnerRequest
  extends AbstractRequestMessage
{
  public long uid;
  public int isOwner;
  public IRequestMessage createNew()
  {
    return new XocDiaOwnerRequest();
  }
}
