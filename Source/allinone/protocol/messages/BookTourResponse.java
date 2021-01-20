package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class BookTourResponse extends AbstractResponseMessage {
	//public ArrayList<UserEntity> top10 = new ArrayList<UserEntity>();
    public String eRRMess;
    public int tour;
    //public String mess;
    public void setSuccess(int tourID) {
    	//mess = m;
    	tour = tourID;
        mCode = ResponseCode.SUCCESS;
    }
    
    public void setFailure(int aCode, String msg) {
        mCode = ResponseCode.FAILURE;
        eRRMess = msg;
    }

    public IResponseMessage createNew() {
        return new BookTourResponse();
    }
}
