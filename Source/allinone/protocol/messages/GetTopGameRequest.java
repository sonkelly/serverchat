

package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author Dinhpv
 */
public class GetTopGameRequest extends AbstractRequestMessage {
    public int gameId;
    
    public IRequestMessage createNew()
    {
        return new GetTopGameRequest();
    }
}
