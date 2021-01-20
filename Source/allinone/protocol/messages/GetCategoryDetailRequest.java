

package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author Dinhpv
 */
public class GetCategoryDetailRequest extends AbstractRequestMessage {
    public int pageIndex;
    public int categoryId;
    
    public IRequestMessage createNew()
    {
        return new GetCategoryDetailRequest();
    }
}
