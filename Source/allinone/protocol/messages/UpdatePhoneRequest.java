package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class UpdatePhoneRequest extends AbstractRequestMessage
{
    public String phone;
    public String userName;
    
    public UpdatePhoneRequest() {
    
    }

    public IRequestMessage createNew()
    {
        return new UpdatePhoneRequest();
    }

	public UpdatePhoneRequest(String phone, String userName) {
		this.phone = phone;
		this.userName = userName;
	}
}