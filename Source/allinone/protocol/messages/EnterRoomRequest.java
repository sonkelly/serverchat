package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class EnterRoomRequest extends AbstractRequestMessage
{
    public int roomID;
    public IRequestMessage createNew()
    {
        return new EnterRoomRequest();
    }
}
