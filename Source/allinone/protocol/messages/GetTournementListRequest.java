package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetTournementListRequest extends AbstractRequestMessage {

	public IRequestMessage createNew() {
		return new GetTournementListRequest();
	}
}