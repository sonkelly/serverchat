package allinone.protocol.messages;





import java.util.List;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.CommentEntity;
import allinone.data.ResponseCode;

public class GetBlogDetailResponse extends AbstractResponseMessage {
        public String errMsg;
        public String title;
        public String post;
        public long blogId;
	public List<CommentEntity> comments;
        public int commentCount;

	public void setFailure(String message)
        {
            mCode = ResponseCode.FAILURE;
            errMsg = message;
            
        }

	public IResponseMessage createNew() {
		return new GetBlogDetailResponse();
	}
}
