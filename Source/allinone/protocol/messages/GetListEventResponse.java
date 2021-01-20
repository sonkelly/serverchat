package allinone.protocol.messages;


import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class GetListEventResponse extends AbstractResponseMessage {
	
	public String message;
	public String value;
        
        
        public IResponseMessage createNew()
        {
            return new GetListEventResponse();
        }

        
}
