package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetFriendRequest extends AbstractRequestMessage
{

    
    public long userId;
    @Override
    public IRequestMessage createNew()
    {
        return new GetFriendRequest();
    }

}
