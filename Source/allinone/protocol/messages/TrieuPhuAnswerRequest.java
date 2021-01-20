package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class TrieuPhuAnswerRequest extends AbstractRequestMessage {
    
    public long mMatchId;
    public String answer;
    
    public IRequestMessage createNew()
    {
        return new TrieuPhuAnswerRequest();
    }
}
