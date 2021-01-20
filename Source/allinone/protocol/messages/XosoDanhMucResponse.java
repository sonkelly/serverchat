package allinone.protocol.messages;

import java.util.ArrayList;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;
import allinone.data.Triple;

public class XosoDanhMucResponse extends AbstractResponseMessage {
	public String errMsg;
	public ArrayList<Triple<Integer, String,Boolean>> data = new ArrayList<Triple<Integer, String,Boolean>>();
	
	public void setSuccess(ArrayList<Triple<Integer, String,Boolean>> d){
		mCode = ResponseCode.SUCCESS;
		data = d;
	}
	public void setFailure(String message) {
		mCode = ResponseCode.FAILURE;
		this.errMsg = message;
	}

	public IResponseMessage createNew() {
		return new XosoDanhMucResponse();
	}
}
