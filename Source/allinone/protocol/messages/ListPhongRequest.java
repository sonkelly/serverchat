package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class ListPhongRequest extends AbstractRequestMessage
{
    public int zoneID;
    public int level = 0;
    public int cacheVersion;
    public IRequestMessage createNew()
    {
        return new ListPhongRequest();
    }
}
