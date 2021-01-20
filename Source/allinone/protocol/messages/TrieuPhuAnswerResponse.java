package allinone.protocol.messages;


import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class TrieuPhuAnswerResponse extends AbstractResponseMessage {
    
    public String value;
    public String mErrorMsg;
    
    public void setFailure(String aErrorMsg)
    {
        mCode = ResponseCode.FAILURE;
        mErrorMsg = aErrorMsg;
    }
    
    public void setSuccess(String msg)
    {
        mCode = ResponseCode.SUCCESS;
        value = msg;
    }
    
    public IResponseMessage createNew()
    {
        return new TrieuPhuAnswerResponse();
    }
}
