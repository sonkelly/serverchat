package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class AccountKitResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public String msg;
    public int errorCode = 0;

    public void setSuccess(int aCode, String value) {
        mCode = aCode;
        this.msg = value;
    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew() {
        return new AccountKitResponse();
    }
}
