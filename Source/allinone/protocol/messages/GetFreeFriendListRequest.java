package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetFreeFriendListRequest extends AbstractRequestMessage
{    
    public int type;
  
    @Override
    public IRequestMessage createNew()
    {
        return new GetFreeFriendListRequest();
    }
}
