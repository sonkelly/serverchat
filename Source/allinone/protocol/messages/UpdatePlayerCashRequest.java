package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class UpdatePlayerCashRequest extends AbstractRequestMessage
{
    public long cash;
    public long matchId;
    
    public UpdatePlayerCashRequest() {
    }

    public IRequestMessage createNew()
    {
        return new UpdatePlayerCashRequest();
    }

	public UpdatePlayerCashRequest(long matchId, long cash) {
		this.matchId = matchId;
		this.cash 	 = cash;
	}
}
