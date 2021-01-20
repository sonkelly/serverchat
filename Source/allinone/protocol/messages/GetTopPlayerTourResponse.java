package allinone.protocol.messages;

import java.util.ArrayList;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;

public class GetTopPlayerTourResponse extends AbstractResponseMessage {
	public ArrayList<UserEntity> top10 = new ArrayList<UserEntity>();
    public String eRRMess;
    public void setSuccess(ArrayList<UserEntity> t) {
    	top10 = t;
        mCode = ResponseCode.SUCCESS;
    }
    
    public void setFailure(int aCode, String msg) {
        mCode = ResponseCode.FAILURE;
        eRRMess = msg;
    }

    public IResponseMessage createNew() {
        return new GetTopPlayerTourResponse();
    }
}
