package allinone.protocol.messages;

import java.util.Hashtable;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class GetRoomMoneyResponse extends AbstractResponseMessage
{

    public String mErrorMsg;
    
    public Hashtable<Integer, Long> moneys;
    public void setFailure(int aCode, String aErrorMsg)
    {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public void setSuccess(int aCode, Hashtable<Integer, Long> m)
    {
    	mCode = aCode;
    	moneys = m;
    }

    public IResponseMessage createNew()
    {
        return new GetRoomMoneyResponse();
    }
}
