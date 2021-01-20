package allinone.protocol.messages;


import java.util.List;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.BlogEntity;
import allinone.data.ResponseCode;

public class GetBlogAccountResponse extends AbstractResponseMessage {
        public String errMsg;
	public List<BlogEntity> blogs;
        public int numBlog;
        public String value;

	public void setFailure(String message)
        {
            mCode = ResponseCode.FAILURE;
            errMsg = message;
            
        }

	public IResponseMessage createNew() {
		return new GetBlogAccountResponse();
	}
}
