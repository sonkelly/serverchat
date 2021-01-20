

package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author Dinhpv
 */
public class BotRequest extends AbstractRequestMessage {

    public int botType;
    
    public IRequestMessage createNew()
    {
        return new BotRequest();
    }
}
