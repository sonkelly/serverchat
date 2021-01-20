package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class MuaTruongRequest extends AbstractRequestMessage
{

    public long matchID;

    public IRequestMessage createNew()
    {
        return new MuaTruongRequest();
    }
}
