package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class TrieuPhuMultiModeResponse extends AbstractResponseMessage {

    public String errMgs;

    public IResponseMessage createNew() {
        return new TrieuPhuMultiModeResponse();
    }

    public void setFailure(String aErrorMsg) {
        mCode = ResponseCode.FAILURE;
        errMgs = aErrorMsg;
    }

    public void setSuccess() {
        mCode = ResponseCode.SUCCESS;
    }
}
