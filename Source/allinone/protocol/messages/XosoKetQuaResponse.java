package allinone.protocol.messages;

import java.util.ArrayList;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class XosoKetQuaResponse extends AbstractResponseMessage {
	public String errMsg;
	public ArrayList<String> data = new ArrayList<String>();
	public boolean isPlay;
	public String date;
	public int numberResult;
	public void setSuccess(ArrayList<String> d, boolean pl, String da, int number){
		numberResult = number;
		date = da;
		isPlay = pl;
		mCode = ResponseCode.SUCCESS;
		data = d;
	}
	
	public void setFailure(String message) {
		mCode = ResponseCode.FAILURE;
		this.errMsg = message;
	}

	public IResponseMessage createNew() {
		return new XosoKetQuaResponse();
	}
}
