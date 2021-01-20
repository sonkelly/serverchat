package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class ChanBocChanResponse extends AbstractResponseMessage {
    public String mErrorMsg;
    public int card;
    public long uid;
    public long chiuUID = 0;
    public void setSuccess(int c, long id, long chiuID){
    	chiuUID = chiuID; 
    	mCode = ResponseCode.SUCCESS;
    	card = c;
    	uid = id;
    }

    public void setFailure(String mess){
    	mCode = ResponseCode.FAILURE;
    	mErrorMsg = mess;
    }

    public IResponseMessage createNew() {
        return new ChanBocChanResponse();
    }

}
