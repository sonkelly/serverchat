package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class BuyAvatarResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public String value;

    public void setSuccess(String v) {
        mCode = ResponseCode.SUCCESS;
        value = v;
    }

    public void setFailure(String aErrorMsg) {
        mCode = ResponseCode.FAILURE;
        mErrorMsg = aErrorMsg;
    }

    @Override
    public IResponseMessage createNew() {
        return new BuyAvatarResponse();
    }
}
