package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetMostPlayingRequest extends AbstractRequestMessage
{

    public IRequestMessage createNew()
    {
        return new GetMostPlayingRequest();
    }
}
