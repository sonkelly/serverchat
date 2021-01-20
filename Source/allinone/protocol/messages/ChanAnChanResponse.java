package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class ChanAnChanResponse extends AbstractResponseMessage {
    public String mErrorMsg;
    public int card;
    public long uid;
    //public boolean isChan;
    public int errorCode;
    public void setSuccess(int c, long id/*, boolean b*/, int code){
    	mCode = ResponseCode.SUCCESS;
    	card = c;
    	uid = id;
    	errorCode = code;
    	//isChan = b;
    }

    public void setFailure(String mess){
    	mCode = ResponseCode.FAILURE;
    	mErrorMsg = mess;
    }

    public IResponseMessage createNew() {
        return new ChanAnChanResponse();
    }

}
