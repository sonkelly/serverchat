package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class ItemListRequest extends AbstractRequestMessage {

    public int itemType = 0;

    @Override
    public IRequestMessage createNew() {
        return new ItemListRequest();
    }

}
