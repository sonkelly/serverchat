package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class SuggestRequest extends AbstractRequestMessage {
    
    public long uid;
    public String note;
    public IRequestMessage createNew()
    {
        return new SuggestRequest();
    }
}
