package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class XosoDanhMucRequest extends AbstractRequestMessage {

    public int id;
    public IRequestMessage createNew()
    {
        return new XosoDanhMucRequest();
    }
}
