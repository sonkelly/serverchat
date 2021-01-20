package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class ChangePasswordLevel2Response extends AbstractResponseMessage
{

    public String mErrorMsg;
    public String value;

    public void setSuccess(String value)
    {
        mCode = ResponseCode.SUCCESS;
        this.value  = value;
    }

    public void setFailure(String aErrorMsg)
    {
        mCode = ResponseCode.FAILURE;
        mErrorMsg = aErrorMsg;
    }
    
    @Override
    public IResponseMessage createNew()
    {
        return new ChangePasswordLevel2Response();
    }
}
