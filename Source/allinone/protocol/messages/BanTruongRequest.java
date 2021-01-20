package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class BanTruongRequest extends AbstractRequestMessage
{

    public long matchID;
    public long money;

    public IRequestMessage createNew()
    {
        return new BanTruongRequest();
    }
}
