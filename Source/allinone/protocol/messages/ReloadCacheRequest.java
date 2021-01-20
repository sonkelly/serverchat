

package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author Dinhpv
 */
public class ReloadCacheRequest extends AbstractRequestMessage {

    
    
    public IRequestMessage createNew()
    {
        return new ReloadCacheRequest();
    }
}
