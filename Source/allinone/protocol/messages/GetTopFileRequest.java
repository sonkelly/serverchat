

package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author Dinhpv
 */
public class GetTopFileRequest extends AbstractRequestMessage {
    public int page;
    public int size;
    
    public IRequestMessage createNew()
    {
        return new GetTopFileRequest();
    }
}
