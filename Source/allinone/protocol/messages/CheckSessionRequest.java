package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class CheckSessionRequest extends AbstractRequestMessage {
    public String ip;
    public IRequestMessage createNew()
    {
        return new CheckSessionRequest();
    }
}
