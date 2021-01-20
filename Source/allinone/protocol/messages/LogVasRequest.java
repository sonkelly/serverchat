package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class LogVasRequest extends AbstractRequestMessage {

    public int page = 1;
    public int typeMoney = -1;//1 tien quan, 0 tien xu
    public IRequestMessage createNew() {
        return new LogVasRequest();
    }

}
