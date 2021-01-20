package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetPlayingListRequest extends AbstractRequestMessage
{

    public int mOffset;
    public int mLength;

    public IRequestMessage createNew()
    {
        return new GetPlayingListRequest();
    }

}
