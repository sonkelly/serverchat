package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class PikachuHelpRequest extends AbstractRequestMessage {
    public long mMatchId;
    public boolean isHelp; // false : revert table
    public IRequestMessage createNew() {
        return new PikachuHelpRequest();
    }
}
