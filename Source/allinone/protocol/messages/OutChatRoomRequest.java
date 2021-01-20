package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class OutChatRoomRequest extends AbstractRequestMessage
{

    public int chatRoomId;
    
    public IRequestMessage createNew()
    {
        return new OutChatRoomRequest();
    }

}
