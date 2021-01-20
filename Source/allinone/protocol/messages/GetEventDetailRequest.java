package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetEventDetailRequest extends AbstractRequestMessage {
    public int eventId;
    public IRequestMessage createNew()
    {
        return new GetEventDetailRequest();
    }
}
