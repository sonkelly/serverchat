package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class CheckSessionResponse extends AbstractResponseMessage {
	
	public String value;
	public String errMsg;
	
	
    public void setSuccess(String value)
    {
        mCode = ResponseCode.SUCCESS;
        this.value = value;
    }
    public void setFailure(String msg){
    	mCode = ResponseCode.FAILURE;
    	errMsg = msg;
    }
    public IResponseMessage createNew()
    {
        return new CheckSessionResponse();
    }
}
