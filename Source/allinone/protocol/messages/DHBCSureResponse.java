package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class DHBCSureResponse extends AbstractResponseMessage {
	public String mErrorMsg;
	public void setFailure(String aErrorMsg) {
		mCode = ResponseCode.FAILURE;
		mErrorMsg = aErrorMsg;
	}

	public void setSuccess() {
		mCode = ResponseCode.SUCCESS;
	}
	public IResponseMessage createNew() {
		return new DHBCSureResponse();
	}
}
