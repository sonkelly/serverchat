package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetAlbumsRequest extends AbstractRequestMessage
{

    public long mUid;

    public IRequestMessage createNew()
    {
        return new GetAlbumsRequest();
    }

}
