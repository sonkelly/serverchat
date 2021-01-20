package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class BotFastPlayRequest extends AbstractRequestMessage {
    public int zoneId;
    public IRequestMessage createNew()
    {
        return new BotFastPlayRequest();
    }
}
