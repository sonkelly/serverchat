package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetFrientListRequest extends AbstractRequestMessage
{

    
    public int pageIndex;
    public int status;
    public IRequestMessage createNew()
    {
        return new GetFrientListRequest();
    }

}
