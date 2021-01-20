package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetChatRoomRequest extends AbstractRequestMessage
{

    public int cacheVersion;

    public IRequestMessage createNew()
    {
        return new GetChatRoomRequest();
    }

}
