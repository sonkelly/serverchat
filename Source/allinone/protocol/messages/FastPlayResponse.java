package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class FastPlayResponse extends AbstractResponseMessage {
	
	public String message;
	public long matchID;
	public int tableID;
	
	
    public void setSuccess(int aCode, long match, int index)
    {
        mCode = aCode;
        matchID = match;
        tableID = index;
    }
    public void setFailure(int aCode, String msg){
    	mCode = aCode;
    	message = msg;
    }
    public IResponseMessage createNew()
    {
        return new FastPlayResponse();
    }
}
