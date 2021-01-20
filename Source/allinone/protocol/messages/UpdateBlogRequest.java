package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class UpdateBlogRequest extends AbstractRequestMessage
{
    public String title;
    public String post;
    public String subject;
    public long blogId;
    
    public IRequestMessage createNew()
    {
        return new UpdateBlogRequest();
    }
}
