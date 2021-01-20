package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetCommonPersonRequest extends AbstractRequestMessage
{

    public int type;
    
    public IRequestMessage createNew()
    {
        return new GetCommonPersonRequest();
    }

}
