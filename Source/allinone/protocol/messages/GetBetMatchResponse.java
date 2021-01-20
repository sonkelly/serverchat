package allinone.protocol.messages;

import java.util.List;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.MatchEntity;

public class GetBetMatchResponse extends AbstractResponseMessage {
    
	public List<MatchEntity> matches;

	

	public IResponseMessage createNew() {
		return new GetBetMatchResponse();
	}
}
