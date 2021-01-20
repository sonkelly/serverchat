package allinone.protocol.messages;


import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class GetTopGameResponse extends AbstractResponseMessage {
        public String errMsg;
	
        public String value;
	public void setFailure(String message)
        {
            mCode = ResponseCode.FAILURE;
            errMsg = message;
            
        }
        
        public void setSuccess(String value)
        {
            mCode = ResponseCode.SUCCESS;
            this.value = value;
            
        }

	public IResponseMessage createNew() {
		return new GetTopGameResponse();
	}
}
