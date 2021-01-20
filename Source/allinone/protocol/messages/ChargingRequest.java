package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class ChargingRequest extends AbstractRequestMessage {
    public int partnerId;
    public String refCode ="0";
    public IRequestMessage createNew() {
        return new ChargingRequest();
    }
}
