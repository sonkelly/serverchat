package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetBestPlayerRequest extends AbstractRequestMessage
{
	public int type; // 1 rich, 2 expr, 3 total play, 4 total win

	public IRequestMessage createNew()
    {
        return new GetBestPlayerRequest();
    }
	
}
