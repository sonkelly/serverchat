package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class TrieuPhuMultiModeRequest extends AbstractRequestMessage {

    public long mMatchId;
    public IRequestMessage createNew() {
        return new TrieuPhuMultiModeRequest();
    }
}
