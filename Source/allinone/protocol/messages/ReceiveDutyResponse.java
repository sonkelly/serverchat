package allinone.protocol.messages;



import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class ReceiveDutyResponse extends AbstractResponseMessage
{

    public String mErrorMsg;
//    public String value;
    
    public void setSuccess()
    {
        mCode = ResponseCode.SUCCESS;
//        this.value = value;
    }

    public void setFailure( String aErrorMsg)
    {
        mCode = ResponseCode.FAILURE;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew()
    {
        return new ReceiveDutyResponse();
    }
}
