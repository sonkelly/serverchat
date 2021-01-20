package allinone.protocol.messages;

import java.util.ArrayList;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.Couple;
import allinone.data.ResponseCode;
import allinone.data.Triple;

public class XosoChoiHistoryResponse extends AbstractResponseMessage {
	public String errMsg;
	public String date;
	//                        Tinh                     Type    Number  Money 
	public ArrayList<Couple<String, ArrayList<Triple<Integer, Integer, Long>>>> data = new ArrayList<Couple<String, ArrayList<Triple<Integer, Integer, Long>>>>();
	public void setSuccess(String da, ArrayList<Couple<String, ArrayList<Triple<Integer, Integer, Long>>>> d){
		date = da;
		data = d;
		mCode = ResponseCode.SUCCESS;
	}
	public void setFailure(String message) {
		mCode = ResponseCode.FAILURE;
		this.errMsg = message;
	}

	public IResponseMessage createNew() {
		return new XosoChoiHistoryResponse();
	}
}
