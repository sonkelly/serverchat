package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class BidaRequest extends AbstractRequestMessage
{

    public int type;//1 get list cue, 2 chagne cue
    public int cueId;
    public int pageIndex;

    public IRequestMessage createNew()
    {
        return new BidaRequest();
    }
}
