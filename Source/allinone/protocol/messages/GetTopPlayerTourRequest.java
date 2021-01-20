package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetTopPlayerTourRequest extends AbstractRequestMessage {
	public int tourID;

	public IRequestMessage createNew() {
		return new GetTopPlayerTourRequest();
	}
}
