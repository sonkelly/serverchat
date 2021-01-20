package allinone.protocol.messages;
import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class IAPResponse extends AbstractResponseMessage {

    public String errMgs;
    public String value = null;
    
    public IResponseMessage createNew() {
        return new IAPResponse();
    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        errMgs = aErrorMsg;
    }
    public void setSuccess(int aCode, String value) {
        mCode = aCode;
        this.value = value;
    }
}
