package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class CancelShowHandResponse
        extends AbstractResponseMessage {

    public String msg;

    public CancelShowHandResponse() {
    }

    public void setSuccess(String msg) {
        mCode = 1;
        this.msg = msg;
    }

    public void setFailure(String msg) {
        mCode = 0;
        this.msg = msg;
    }

    public IResponseMessage createNew() {
        return new CancelShowHandResponse();
    }
}
