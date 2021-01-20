package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class SoChiResponse extends AbstractResponseMessage {
	
	public String message;
	public String value;
	
    public void setSuccess(long u, String value)
    {
        mCode = ResponseCode.SUCCESS;
        this.value = value;
    }
    
    public void setFailure(String msg){
        mCode = ResponseCode.FAILURE;
        message = msg;
    }
    
    public IResponseMessage createNew()
    {
        return new SoChiResponse();
    }
}
