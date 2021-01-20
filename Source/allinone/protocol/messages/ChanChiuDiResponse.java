package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class ChanChiuDiResponse extends AbstractResponseMessage {
	public String mErrorMsg;
	public long uid;
	public void setFailure(String aErrorMsg) {
		mCode = ResponseCode.FAILURE;
		mErrorMsg = aErrorMsg;
	}

	public void setSuccess(long id) {
		mCode = ResponseCode.SUCCESS;
		uid = id;
	}
	public IResponseMessage createNew() {
		return new ChanChiuDiResponse();
	}
}