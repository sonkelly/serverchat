package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author Dinhpv
 */
public class JoinPlayerRequest extends AbstractRequestMessage {

    public long mMatchId;

    public IRequestMessage createNew() {
        return new JoinPlayerRequest();
    }
}
