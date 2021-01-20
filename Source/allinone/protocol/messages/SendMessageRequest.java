package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class SendMessageRequest extends AbstractRequestMessage {

    public long sUID;
    public String message;
    public long dUID;
    public String title;
    public IRequestMessage createNew() {
        return new SendMessageRequest();
    }
}
