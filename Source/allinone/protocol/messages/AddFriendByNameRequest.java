package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class AddFriendByNameRequest extends AbstractRequestMessage
{
	public String friendName;
    public IRequestMessage createNew()
    {
        return new AddFriendByNameRequest();
    }
}
