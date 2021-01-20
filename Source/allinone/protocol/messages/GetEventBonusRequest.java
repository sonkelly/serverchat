package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetEventBonusRequest extends AbstractRequestMessage {
	
    public IRequestMessage createNew()
    {
        return new GetEventBonusRequest();
    }
}
