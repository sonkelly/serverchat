package allinone.protocol.messages;

import java.util.ArrayList;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.MatchEntity;
import allinone.data.ResponseCode;

public class GetBetResultResponse extends AbstractResponseMessage {
    
	public ArrayList<MatchEntity> matches;
	public String mErrorMsg;
	public void setSuccess(ArrayList<MatchEntity> m) {
		mCode = ResponseCode.SUCCESS;
		matches = m;
	}
	public void setFailure(String m){
		mCode = ResponseCode.FAILURE;
		mErrorMsg = m;
	}
	public IResponseMessage createNew() {
		return new GetBetResultResponse();
	}
}
