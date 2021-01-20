package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class BuyLevelRequest extends AbstractRequestMessage
{
    public int level;
    public long uid;
    public IRequestMessage createNew()
    {
        return new BuyLevelRequest();
    }
}
