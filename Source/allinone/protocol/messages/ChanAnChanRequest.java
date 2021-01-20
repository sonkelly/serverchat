package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;




public class ChanAnChanRequest extends AbstractRequestMessage {
	public int card;
	public IRequestMessage createNew() {
		return new ChanAnChanRequest();
	}
}
