package allinone.protocol.messages;


import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class TrieuPhuHelpResponse extends AbstractResponseMessage {
    
    public String mErrorMsg;
    public String value;
    
    public void setFailure(int aCode, String aErrorMsg)
    {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }
    public void setSuccess(int aCode)
    {
        mCode = aCode;
    }
    public IResponseMessage createNew()
    {
        return new TrieuPhuHelpResponse();
    }
}
