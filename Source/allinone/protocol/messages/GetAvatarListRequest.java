package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetAvatarListRequest extends AbstractRequestMessage
{
    @Override
    public IRequestMessage createNew()
    {
        return new GetAvatarListRequest();
    }
}
