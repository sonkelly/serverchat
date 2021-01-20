package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class ItemHistoryRequest extends AbstractRequestMessage {

    public int page = 1;

    public IRequestMessage createNew() {
        return new ItemHistoryRequest();
    }

}
