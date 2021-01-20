package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetFollowRequest extends AbstractRequestMessage
{

    
    public int pageIndex;
    
    public IRequestMessage createNew()
    {
        return new GetFollowRequest();
    }

}
