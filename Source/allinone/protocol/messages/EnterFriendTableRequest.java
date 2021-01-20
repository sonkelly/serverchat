package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class EnterFriendTableRequest extends AbstractRequestMessage
{
 
    public long friendId;
   
    public IRequestMessage createNew()
    {
        return new EnterFriendTableRequest();
    }
}
