package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetWaitingListRequest extends AbstractRequestMessage
{

    public int mOffset;
    public int mLength;
    public int level;
    public int minLevel;
    public IRequestMessage createNew()
    {
        return new GetWaitingListRequest();
    }

}
