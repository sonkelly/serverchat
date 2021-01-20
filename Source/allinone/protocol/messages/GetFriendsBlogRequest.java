

package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author Dinhpv
 */
public class GetFriendsBlogRequest extends AbstractRequestMessage {
    public int page;
    public IRequestMessage createNew()
    {
        return new GetFriendsBlogRequest();
    }
}
