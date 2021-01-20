package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class GetBonusCashResponse extends AbstractResponseMessage
{

    public String mErrorMsg;
    public long money;
    
    public void setFailure(int aCode, String aErrorMsg)
    {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public void setSuccess(int aCode, long money, String message)
    {
        mCode = aCode;
        this.money = money;
        mErrorMsg = message;
    }

    public IResponseMessage createNew()
    {
        return new GetBonusCashResponse();
    }
}
