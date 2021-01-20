

package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author Dinhpv
 */
public class GetBlogAccountRequest extends AbstractRequestMessage {
    public int page;
    public long userId;
    public int size =0;
    public IRequestMessage createNew()
    {
        return new GetBlogAccountRequest();
    }
}
