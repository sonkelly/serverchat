package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetResourceRequest extends AbstractRequestMessage {
    
    public int type;
    public int itemId;
    
	
    public IRequestMessage createNew()
    {
        return new GetResourceRequest();
    }
}
