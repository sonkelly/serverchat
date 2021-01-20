package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class UpdateVersionResponse extends AbstractResponseMessage {

    public String message;
    public String link;
    public int currentVersion;
    
    public void setSuccess() {
        mCode = ResponseCode.SUCCESS;
    }

    public UpdateVersionResponse() {
    }

    public void setFailure(int aCode, String msg) {
        mCode = aCode;
        message = msg;
    }
    
    public IResponseMessage createNew() {
        return new UpdateVersionResponse();
    }
}
