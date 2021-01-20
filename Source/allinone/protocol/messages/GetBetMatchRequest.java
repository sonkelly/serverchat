

package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author Dinhpv
 */
public class GetBetMatchRequest extends AbstractRequestMessage {

    public int league;
    
    public IRequestMessage createNew()
    {
        return new GetBetMatchRequest();
    }
}
