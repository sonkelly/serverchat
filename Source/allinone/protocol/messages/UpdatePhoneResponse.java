package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class UpdatePhoneResponse extends AbstractResponseMessage
{

    public String mErrorMsg;
    public String mess;
    
    public void setSuccess(int aCode, String mess)
    {
        mCode = aCode;
        this.mess = mess;
    }
    
    public void setFailure(int aCode, String aErrorMsg)
    {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew()
    {
        return new UpdatePhoneResponse();
    }
}
