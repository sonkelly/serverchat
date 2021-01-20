package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetUserAccountRequest extends AbstractRequestMessage
{

    public long uid;
    
    public IRequestMessage createNew()
    {
        return new GetUserAccountRequest();
    }

}
