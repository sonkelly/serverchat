package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetUserDataRequest extends AbstractRequestMessage
{
    public long userId;
    
    @Override
    public IRequestMessage createNew()
    {
        return new GetUserDataRequest();
    }

}
