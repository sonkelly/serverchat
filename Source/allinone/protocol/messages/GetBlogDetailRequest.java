

package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author Dinhpv
 */
public class GetBlogDetailRequest extends AbstractRequestMessage {
    public long blogId;
    public int page;
    public boolean needTitle;
    public IRequestMessage createNew()
    {
        return new GetBlogDetailRequest();
    }
}
