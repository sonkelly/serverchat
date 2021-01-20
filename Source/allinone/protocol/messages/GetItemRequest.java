package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetItemRequest extends AbstractRequestMessage {
    public int cacheVersion;
    public IRequestMessage createNew()
    {
        return new GetItemRequest();
    }
}
