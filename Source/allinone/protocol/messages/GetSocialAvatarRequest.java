package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetSocialAvatarRequest extends AbstractRequestMessage
{
    public int type;
    public long uid;
    public IRequestMessage createNew()
    {
        return new GetSocialAvatarRequest();
    }
}
