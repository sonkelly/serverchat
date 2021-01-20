package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class SetSttRequest extends AbstractRequestMessage
{
    
    public String status;
    public IRequestMessage createNew()
    {
        return new SetSttRequest();
    }
}
