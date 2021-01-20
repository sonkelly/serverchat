package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;
public class ChanChiuChanRequest extends AbstractRequestMessage {
	public long uid = 0;
	public IRequestMessage createNew() {
		return new ChanChiuChanRequest();
	}
}
