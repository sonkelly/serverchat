package allinone.protocol.messages;

import java.util.ArrayList;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.Couple;
import allinone.data.ResponseCode;

public class DanhMucBoiResponse extends AbstractResponseMessage {

	public ArrayList<Couple<Integer, String>> danhMuc;
	public String errMsg;


	public void setSuccess(ArrayList<Couple<Integer, String>> d) {
		this.mCode = ResponseCode.SUCCESS;
		danhMuc = d;
	}

	public void setFailure(String errorMsg) {
		this.mCode = ResponseCode.FAILURE;
		this.errMsg = errorMsg;
	}

	public IResponseMessage createNew() {
		return new DanhMucBoiResponse();
	}
}
