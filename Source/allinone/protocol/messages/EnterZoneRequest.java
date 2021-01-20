package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class EnterZoneRequest extends AbstractRequestMessage
{
    public int zoneID;
    public int cacheVersion;
    public IRequestMessage createNew()
    {
        return new EnterZoneRequest();
    }
}
