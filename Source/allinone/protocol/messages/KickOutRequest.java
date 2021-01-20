package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class KickOutRequest extends AbstractRequestMessage {
	
    public long mMatchId;
    public long uid;

    public IRequestMessage createNew()
    {
        return new KickOutRequest();
    }
}
