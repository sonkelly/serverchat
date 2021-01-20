package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class BoiConfirmRequest extends AbstractRequestMessage {   
	public String code;
	public int boiCode;
    public IRequestMessage createNew()
    {
        return new BoiRequest();
    }
}
