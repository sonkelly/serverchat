package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetFileDetailRequest extends AbstractRequestMessage
{

    public long fileId;
   
    

    public IRequestMessage createNew()
    {
        return new GetFileDetailRequest();
    }

}
