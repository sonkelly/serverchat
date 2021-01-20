package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class GetImageResponse  extends AbstractResponseMessage {

    public String mErrorMsg;
    public String image;
    public String name;
    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public void setSuccess(int aCode, String i, String n) {
        mCode = aCode;
        this.image = i;
        this.name = n;
    }

    public IResponseMessage createNew() {
        return new GetImageResponse();
    }

}
