package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class QuayCNKDRequest extends AbstractRequestMessage
{
    
    
    public IRequestMessage createNew()
    {
        return new QuayCNKDRequest();
    }
}
