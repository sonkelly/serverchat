

package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author Dinhpv
 */
public class GetNewsDetailRequest extends AbstractRequestMessage {
    public int pageIndex;
    public long newsId;
    public int categoryId;
    
    public IRequestMessage createNew()
    {
        return new GetNewsDetailRequest();
    }
}
