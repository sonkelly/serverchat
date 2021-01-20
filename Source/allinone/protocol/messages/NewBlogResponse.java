package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class NewBlogResponse extends AbstractResponseMessage {
	public String message;
        public long blogId;
    public void setSuccess(int aCode)
    {
        mCode = aCode;
    }
    public void setFailure(int aCode, String msg){
    	mCode = aCode;
    	message = msg;
    }
    
    public IResponseMessage createNew()
    {
        return new NewBlogResponse();
    }
}
