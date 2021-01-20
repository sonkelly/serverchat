package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class VivuAppearRequest extends AbstractRequestMessage {

    public IRequestMessage createNew() {
        return new VivuAppearRequest();
    }
}
