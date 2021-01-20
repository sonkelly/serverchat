

package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author Dinhpv
 */
public class AcceptJoinPlayerRequest extends AbstractRequestMessage {

    public long mMatchId;
    public long uid;
    public boolean isAccept;
    public IRequestMessage createNew()
    {
        return new AcceptJoinPlayerRequest();
    }
}
