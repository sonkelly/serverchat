package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class PikachuHelpResponse extends AbstractResponseMessage {

	public String mErrorMsg;
	public boolean isHelp;

	public void setFailure(int aCode, String aErrorMsg) {
		mCode = aCode;
		mErrorMsg = aErrorMsg;
	}

	public void setSuccess(boolean h) {
		isHelp = h;
		mCode = ResponseCode.SUCCESS;
	}

	public IResponseMessage createNew() {
		return new PikachuHelpResponse();
	}
}
