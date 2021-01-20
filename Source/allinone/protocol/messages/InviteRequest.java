package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class InviteRequest extends AbstractRequestMessage
{

    public long roomID;
    public long destUid;
    public long sourceUid;


    public IRequestMessage createNew()
    {
        return new InviteRequest();
    }
}
