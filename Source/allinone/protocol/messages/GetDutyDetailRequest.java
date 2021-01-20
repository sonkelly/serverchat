package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetDutyDetailRequest extends AbstractRequestMessage {
	
    public int dutyId;

    public IRequestMessage createNew()
    {
        return new GetDutyDetailRequest();
    }
}
