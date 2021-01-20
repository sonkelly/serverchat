package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class EnterChatRoomRequest extends AbstractRequestMessage
{

    
    public int chatRoomId;
    
    public IRequestMessage createNew()
    {
        return new EnterChatRoomRequest();
    }

}
