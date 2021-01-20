package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class ChatZoneRequest extends AbstractRequestMessage
{

    public int zoneid ;
    public String mMessage;
    public int type;
    public int pageIndex;
    public int zoneId;
    public IRequestMessage createNew()
    {
        return new ChatZoneRequest();
    }
}
