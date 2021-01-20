package allinone.protocol.messages;


import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class GetTopBlogResponse extends AbstractResponseMessage {
        public String errMsg;
	
        public String value;
	public void setFailure(String message)
        {
            mCode = ResponseCode.FAILURE;
            errMsg = message;
            
        }

	public IResponseMessage createNew() {
		return new GetTopBlogResponse();
	}
}
