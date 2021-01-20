package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetLogsRequest
  extends AbstractRequestMessage
{
  public long money;
  public int betside;//0 le, 1 chan, 2 4trang, 3 4den, 4 4trang1den, 5 3den1trang
  
  @Override
  public IRequestMessage createNew()
  {
    return new GetLogsRequest();
  }
}
