package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class ItemOrderRequest extends AbstractRequestMessage
{

    public long itemId;
    public String pass;
    public IRequestMessage createNew()
    {
        return new ItemOrderRequest();
    }
}
