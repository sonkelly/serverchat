package allinone.protocol.messages;


import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class GetPersonInfoResponse extends AbstractResponseMessage {
        public String errMsg;
	
        public String value;
	public void setFailure(String message)
        {
            mCode = ResponseCode.FAILURE;
            errMsg = message;
            
        }
        
        public void setSuccess(String message)
        {
            mCode = ResponseCode.SUCCESS;
            value = message;
            
        }
        

	public IResponseMessage createNew() {
		return new GetPersonInfoResponse();
	}
}
