package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class SendAdvResponse extends AbstractResponseMessage {

    public String message;

    public int type = 0;
    public long activateTime = 0;
    public void setSuccess(String msg, int type) {
        mCode = ResponseCode.SUCCESS;
        this.message = msg;
        this.type = type;
    }

    public SendAdvResponse() {
    }

    public void setFailure(int aCode, String msg) {
        mCode = aCode;
        message = msg;
    }

    @Override
    public IResponseMessage createNew() {
        return new SendAdvResponse();
    }
}
