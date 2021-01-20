package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetChiecNonRequest extends AbstractRequestMessage {
    
    public int cacheVersion;
	
    public IRequestMessage createNew()
    {
        return new GetChiecNonRequest();
    }
}
