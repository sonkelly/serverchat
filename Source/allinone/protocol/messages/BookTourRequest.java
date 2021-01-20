package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class BookTourRequest extends AbstractRequestMessage {
	public int tourID;

	public IRequestMessage createNew() {
		return new BookTourRequest();
	}
}
