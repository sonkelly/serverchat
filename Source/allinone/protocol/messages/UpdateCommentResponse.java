package allinone.protocol.messages;



import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class UpdateCommentResponse extends AbstractResponseMessage
{

    public String mErrorMsg;
    
    public void setSuccess(int aCode)
    {
        mCode = aCode;
    }

    public void setFailure(int aCode, String aErrorMsg)
    {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew()
    {
        return new UpdateCommentResponse();
    }
}
