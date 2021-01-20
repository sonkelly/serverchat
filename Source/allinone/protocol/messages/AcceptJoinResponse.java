package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class AcceptJoinResponse extends AbstractResponseMessage {

    public String message;

    public void setSuccess(int aCode) {
        mCode = aCode;
    }

    public void setFailure(int aCode, String msg) {
        mCode = aCode;
        message = msg;
    }

    public IResponseMessage createNew() {
        return new AcceptJoinResponse();
    }
}
