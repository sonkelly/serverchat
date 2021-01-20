package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class RestartRequest extends AbstractRequestMessage {

    public long mMatchId;
    public long money;
    public long uid;

    public IRequestMessage createNew() {
        return new RestartRequest();
    }
}
