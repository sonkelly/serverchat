package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class UpdatePlayerCashResponse extends AbstractResponseMessage
{

    public String mErrorMsg;
    public String mess;
    public long userId;
    public long cash;
    
    public void setSuccess(int aCode, long userId, long cash)
    {
        mCode = aCode;
        this.userId = userId;
        this.cash = cash;
    }
    
    public void setFailure(int aCode, String aErrorMsg)
    {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew()
    {
        return new UpdatePlayerCashResponse();
    }
}
