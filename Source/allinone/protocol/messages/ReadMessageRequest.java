package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class ReadMessageRequest extends AbstractRequestMessage
{

    public long messID;

    public IRequestMessage createNew()
    {
        return new ReadMessageRequest();
    }
}
