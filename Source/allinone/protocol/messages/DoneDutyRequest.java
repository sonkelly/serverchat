package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class DoneDutyRequest extends AbstractRequestMessage {
	
    public int dutyId;
    

    public IRequestMessage createNew()
    {
        return new DoneDutyRequest();
    }
}
