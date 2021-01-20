package allinone.protocol.messages;


import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class GetEventBonusResponse extends AbstractResponseMessage {
	
	public String message;
	public String value;
    
	public int id;
	public String imageLink;
	public String actionLink;	
	public int type;
        
    public IResponseMessage createNew()
    {
        return new GetEventBonusResponse();
    }

    public void setSuccess(int aCode, int id, String imageLink, String actionLink, int type ) {
        mCode = aCode;
        this.id = id;
        this.imageLink = imageLink;
        this.actionLink = actionLink;
        this.type = type;
    }

    public void setFailure(int aCode, String msg) {
        mCode = aCode;
        message = msg;
    }    
}
