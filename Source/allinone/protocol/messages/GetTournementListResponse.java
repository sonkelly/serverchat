package allinone.protocol.messages;

import java.util.ArrayList;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;
import allinone.tournement.TournementEntity;
public class GetTournementListResponse extends AbstractResponseMessage {

	
	public String errMsg;
	public ArrayList<TournementEntity> tours = new ArrayList<TournementEntity>();

	public void setFailure(String errorMsg) {
		this.mCode = ResponseCode.FAILURE;
		this.errMsg = errorMsg;
	}

	public void setSuccess(ArrayList<TournementEntity> t){
		tours = t;
		mCode = ResponseCode.SUCCESS;
	}
	public IResponseMessage createNew() {
		return new GetTournementListResponse();
	}

}
