package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetUserInfoRequest extends AbstractRequestMessage
{

    public long mUid;

    public IRequestMessage createNew()
    {
        return new GetUserInfoRequest();
    }

}
