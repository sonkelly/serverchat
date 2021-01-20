package allinone.protocol.messages;


import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class XosoChoiResponse extends AbstractResponseMessage {
	public String errMsg;
	public void setSuccess(){
		mCode = ResponseCode.SUCCESS;
	}
	public void setFailure(String message) {
		mCode = ResponseCode.FAILURE;
		this.errMsg = message;
	}

	public IResponseMessage createNew() {
		return new XosoChoiResponse();
	}
}
