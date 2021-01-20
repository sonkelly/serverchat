

package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author Dinhpv
 */
public class UseCardRequest extends AbstractRequestMessage {

    public String cardId;
    public String cardCode;
    public String serviceId;
    public String refCode;
    public String menhgia;
    @Override
    public IRequestMessage createNew()
    {
        return new UseCardRequest();
    }
}
