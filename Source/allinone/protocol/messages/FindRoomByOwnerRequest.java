package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class FindRoomByOwnerRequest extends AbstractRequestMessage
{
    public String roomOwner;
    public IRequestMessage createNew()
    {
        return new FindRoomByOwnerRequest();
    }
}
