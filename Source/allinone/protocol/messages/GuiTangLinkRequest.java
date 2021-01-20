package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GuiTangLinkRequest extends AbstractRequestMessage {
    
    public String phoneNumber;
    public IRequestMessage createNew()
    {
        return new GuiTangLinkRequest();
    }
}
