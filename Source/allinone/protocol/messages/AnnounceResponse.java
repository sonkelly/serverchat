package allinone.protocol.messages;



import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class AnnounceResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public String message;

    public void setSuccess(int aCode, String aPostList) {
        mCode = aCode;
        message = aPostList;
    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew() {
        return new AnnounceResponse();
    }
}
