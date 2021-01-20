package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetCommentRequest extends AbstractRequestMessage
{

    
    public int systemObjectId;
    public long systemObjectRecordId;
    public int page;
    public int size;
    
    public IRequestMessage createNew()
    {
        return new GetCommentRequest();
    }

}
