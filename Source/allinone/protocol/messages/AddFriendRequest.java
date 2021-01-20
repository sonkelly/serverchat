package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class AddFriendRequest extends AbstractRequestMessage
{
	public long currID;
	public long friendID;
    public IRequestMessage createNew()
    {
        return new AddFriendRequest();
    }
}
