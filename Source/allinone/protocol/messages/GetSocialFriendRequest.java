

package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author Dinhpv
 */
public class GetSocialFriendRequest extends AbstractRequestMessage {
    
    public int pageIndex;
    
    public IRequestMessage createNew()
    {
        return new GetSocialFriendRequest();
    }
}
