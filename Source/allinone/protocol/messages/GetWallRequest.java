package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetWallRequest extends AbstractRequestMessage
{    
    public long uid;
    public int pageIndex = 0;
    public int pageSize = 0;
    
    public IRequestMessage createNew()
    {
        return new GetWallRequest();
    }

}
