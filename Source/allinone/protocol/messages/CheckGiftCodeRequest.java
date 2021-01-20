package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class CheckGiftCodeRequest extends AbstractRequestMessage {
    
    public String giffCode;
    public IRequestMessage createNew()
    {
        return new CheckGiftCodeRequest();
    }
}
