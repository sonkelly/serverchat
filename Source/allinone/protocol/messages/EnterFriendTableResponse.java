package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class EnterFriendTableResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public String value;
    public void setFailure(String aErrorMsg) {
        mCode = ResponseCode.FAILURE;
        mErrorMsg = aErrorMsg;
    }

    public void setSuccess(String value) {
        mCode = ResponseCode.SUCCESS;
        this.value = value;
    }
    
    public IResponseMessage createNew() {
        return new EnterFriendTableResponse();
    }
}
