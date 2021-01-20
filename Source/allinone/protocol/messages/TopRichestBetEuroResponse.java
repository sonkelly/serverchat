package allinone.protocol.messages;

import java.util.ArrayList;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.Triple;

public class TopRichestBetEuroResponse extends AbstractResponseMessage {
    
	public ArrayList<Triple<String, Integer, Long>> users;

	

	public IResponseMessage createNew() {
		return new TopRichestBetEuroResponse();
	}
}
