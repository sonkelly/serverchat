package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class ReceiveDutyRequest extends AbstractRequestMessage {
	
    public int dutyId;

    public IRequestMessage createNew()
    {
        return new ReceiveDutyRequest();
    }
}
