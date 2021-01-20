package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class ReadyRequest extends AbstractRequestMessage{
	public long matchID;
	public long uid;
	public boolean ready;
    public IRequestMessage createNew()
    {
        return new ReadyRequest();
    }
}
