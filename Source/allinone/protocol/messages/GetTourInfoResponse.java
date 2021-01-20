package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class GetTourInfoResponse extends AbstractResponseMessage {
	public String mess;
    public String eRRMess;
    public void setSuccess(String m) {
    	mess = m;
        mCode = ResponseCode.SUCCESS;
    }
    
    public void setFailure(int aCode, String msg) {
        mCode = ResponseCode.FAILURE;
        eRRMess = msg;
    }

    public IResponseMessage createNew() {
        return new GetTourInfoResponse();
    }
}