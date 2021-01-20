package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class AddSocialFriendRequest extends AbstractRequestMessage
{
    
    public long friendID;
    public boolean isConfirmed;
    
    public IRequestMessage createNew()
    {
        return new AddSocialFriendRequest();
    }
}
