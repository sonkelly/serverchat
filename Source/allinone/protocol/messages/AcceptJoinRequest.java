package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class AcceptJoinRequest extends AbstractRequestMessage {
	
    public long mMatchId;
    public long uid;
    public String password;
    public boolean isAccept;
    public IRequestMessage createNew()
    {
        return new AcceptJoinRequest();
    }
}
