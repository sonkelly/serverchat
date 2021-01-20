package allinone.protocol.messages;



import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class NewAlbumResponse extends AbstractResponseMessage
{

    public String mErrorMsg;
    public long albumId;
    
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
        return new NewAlbumResponse();
    }
}
