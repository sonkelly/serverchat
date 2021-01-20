package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class BoiRequest extends AbstractRequestMessage {   
	public int code;
	public String[] data = new String[4];
    public IRequestMessage createNew()
    {
        return new BoiRequest();
    }
}
