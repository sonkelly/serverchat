package allinone.protocol.messages;

import java.util.List;
import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class SessionActionRequest extends AbstractRequestMessage
{
    public int zoneID;
    public int roomID;
  
    public int status;
    
    
    @Override
    public IRequestMessage createNew()
    {
        return new SessionActionRequest();
    }
}
