package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class UpdateUserInfoRequest extends AbstractRequestMessage
{

    public String name;
    public String cmt;
    public String address;
    
    public UpdateUserInfoRequest() {
    	
    }

    public IRequestMessage createNew()
    {
        return new UpdateUserInfoRequest();
    }

	public UpdateUserInfoRequest(String name, String cmt, String address) {
		this.name = name;
		this.cmt = cmt;
		this.address = address;
	}
    
}
