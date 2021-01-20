package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetResultDutyRequest extends AbstractRequestMessage {
	
    public int dutyId;

    public IRequestMessage createNew()
    {
        return new GetResultDutyRequest();
    }
}
