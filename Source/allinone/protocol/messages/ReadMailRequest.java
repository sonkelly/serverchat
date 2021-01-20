package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class ReadMailRequest extends AbstractRequestMessage
{

    public long mailId;

    public IRequestMessage createNew()
    {
        return new ReadMailRequest();
    }
}
