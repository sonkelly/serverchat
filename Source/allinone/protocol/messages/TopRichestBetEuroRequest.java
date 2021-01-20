

package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author Dinhpv
 */
public class TopRichestBetEuroRequest extends AbstractRequestMessage {

    
    
    public IRequestMessage createNew()
    {
        return new TopRichestBetEuroRequest();
    }
}
