package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class SendChatRoomRequest extends AbstractRequestMessage
{

    
    public String content;
    
    public IRequestMessage createNew()
    {
        return new SendChatRoomRequest();
    }

}
