package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class FastLoginRequest extends AbstractRequestMessage {

    public String mobileVersion = "";
    public int deviceType = 4;
    public String deviceID = "";

    @Override
    public IRequestMessage createNew() {
        return new FastLoginRequest();
    }

}
