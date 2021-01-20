package allinone.protocol.messages;

import java.util.List;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ChargingInfo;

public class ChargingResponse extends AbstractResponseMessage {
    public String errMgs;
    public List<ChargingInfo> cardInfo = null;
    public String value = null;
    
    public IResponseMessage createNew() {
        return new ChargingResponse();
    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        errMgs = aErrorMsg;
    }
    
    public void setSuccess(int aCode, String value) {
        mCode = aCode;
        this.value = value;
    }
}
