package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetPersonInfoRequest extends AbstractRequestMessage
{

    public long uid;
    
    public IRequestMessage createNew()
    {
        return new GetPersonInfoRequest();
    }

}
