package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class PrivateChatRequest extends AbstractRequestMessage
{

    public long sourceUid;
    public long destUid;
    public String mMessage;


    public IRequestMessage createNew()
    {
        return new PrivateChatRequest();
    }
}
