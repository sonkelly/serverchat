package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class ItemOrderCancelRequest extends AbstractRequestMessage
{

    public long itemOrderId;

    public IRequestMessage createNew()
    {
        return new ItemOrderCancelRequest();
    }
}
