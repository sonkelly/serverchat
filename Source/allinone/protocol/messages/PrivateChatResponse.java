package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class PrivateChatResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public long sourceID;
    public String message;
    public String username;
    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public void setSuccess(int aCode, long s, String message,String username_) {
        mCode = aCode;
        sourceID = s;
        this.message = message;
        username =username_;
    }
    
    public IResponseMessage createNew() {
        return new PrivateChatResponse();
    }
}
