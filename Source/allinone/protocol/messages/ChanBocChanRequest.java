package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class ChanBocChanRequest extends AbstractRequestMessage
{
	public boolean isAutoWhenHangOver = false; 
	public long uid = 0;
    public IRequestMessage createNew()
    {
        return new ChanBocChanRequest();
    }
}
