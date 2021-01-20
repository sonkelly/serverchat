package allinone.protocol.messages;

import java.util.ArrayList;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.Couple;
import allinone.data.ResponseCode;

public class BoiConfirmResponse extends AbstractResponseMessage {

	public ArrayList<Couple<String, String>> detail;
	public String errMsg;


	public void setSuccess(ArrayList<Couple<String, String>> d) {
		this.mCode = ResponseCode.SUCCESS;
		detail = d;
	}

	public void setFailure(String errorMsg) {
		this.mCode = ResponseCode.FAILURE;
		this.errMsg = errorMsg;
	}

	public IResponseMessage createNew() {
		return new BoiConfirmResponse();
	}
}
