package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class BuyItemRequest extends AbstractRequestMessage
{
    public int itemId;
    
    public IRequestMessage createNew()
    {
        return new BuyItemRequest();
    }
}
