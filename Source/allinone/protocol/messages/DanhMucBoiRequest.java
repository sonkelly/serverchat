package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class DanhMucBoiRequest extends AbstractRequestMessage {   
	public int code;
    public IRequestMessage createNew()
    {
        return new DanhMucBoiRequest();
    }
}
