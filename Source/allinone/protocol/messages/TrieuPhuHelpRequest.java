package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class TrieuPhuHelpRequest extends AbstractRequestMessage {
    
    public long mMatchId;
    public int type;
    
    public IRequestMessage createNew()
    {
        return new TrieuPhuHelpRequest();
    }
}
