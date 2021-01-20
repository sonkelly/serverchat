package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class ItemOrderCancelResponse extends AbstractResponseMessage {
	
	public String mErrorMsg;
    public String value;
	
    public void setSuccess(int aCode, String value) {
		mCode = aCode;
		this.value = value;
	}

	public void setFailure(int aCode, String aErrorMsg) {
		mCode = aCode;
		mErrorMsg = aErrorMsg;
	}

	public IResponseMessage createNew() {
		return new ItemOrderCancelResponse();
	}
}
