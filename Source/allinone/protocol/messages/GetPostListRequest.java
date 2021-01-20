package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetPostListRequest extends AbstractRequestMessage
{

    public IRequestMessage createNew()
    {
        return new GetPostListRequest();
    }
}
