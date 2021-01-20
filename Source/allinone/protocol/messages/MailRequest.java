package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class MailRequest extends AbstractRequestMessage {
    public long uid;
    public int pageIndex;
    public IRequestMessage createNew() {
        return new MailRequest();
    }
}
