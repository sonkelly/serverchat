package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class SetSocialAvatarRequest extends AbstractRequestMessage
{
    public int type;
    public long fileId;
    public IRequestMessage createNew()
    {
        return new SetSocialAvatarRequest();
    }
}
