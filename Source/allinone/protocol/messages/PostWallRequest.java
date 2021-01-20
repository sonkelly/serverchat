package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class PostWallRequest extends AbstractRequestMessage
{
    
    public String mind;
    public long sourceId;
    public long destId;
    public long fileId;
    
    
    public IRequestMessage createNew()
    {
        return new PostWallRequest();
    }
}
