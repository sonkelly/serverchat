package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class BoiResponse extends AbstractResponseMessage {

	public String detail;
	public String errMsg;


	public void setSuccess(String d) {
		this.mCode = ResponseCode.SUCCESS;
		detail = d;
	}

	public void setFailure(String errorMsg) {
		this.mCode = ResponseCode.FAILURE;
		this.errMsg = errorMsg;
	}

	public IResponseMessage createNew() {
		return new BoiResponse();
	}
}
