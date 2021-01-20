package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class AnnounceRequest extends AbstractRequestMessage {

    public String message;

    public IRequestMessage createNew() {
        return new AnnounceRequest();
    }
}
