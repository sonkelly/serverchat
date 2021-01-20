package allinone.protocol.messages;

import java.util.List;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.UserEntity;

public class TopBetEuroPlayerResponse extends AbstractResponseMessage {
    
	public List<UserEntity> users;

	

	public IResponseMessage createNew() {
		return new TopBetEuroPlayerResponse();
	}
}
