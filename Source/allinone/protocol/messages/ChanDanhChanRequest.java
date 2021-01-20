package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class ChanDanhChanRequest extends AbstractRequestMessage {
	public long uid = 0;
	public int card;
	public long destID;//Trả chíu
	public boolean isAutoWhenHangOver = false;
	public IRequestMessage createNew() {
		return new ChanDanhChanRequest();
	}
}
