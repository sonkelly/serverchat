package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class BuyAvatarRequest extends AbstractRequestMessage
{
    public int avatarID;
    @Override
    public IRequestMessage createNew()
    {
        return new BuyAvatarRequest();
    }
}
