

package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author Dinhpv
 */
public class BetEuroMatchRequest extends AbstractRequestMessage {
    public long money;
    public int type;
    public int bet;
    public int matchID;
    
    public IRequestMessage createNew()
    {
        return new BetEuroMatchRequest();
    }
}
