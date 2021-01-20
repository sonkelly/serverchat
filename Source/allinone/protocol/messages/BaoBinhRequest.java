package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class BaoBinhRequest extends AbstractRequestMessage {
	
	public long matchID;
    public boolean isBao;
    public String cardList;
    
    public IRequestMessage createNew()
    {
        return new BaoBinhRequest();
    }
}
