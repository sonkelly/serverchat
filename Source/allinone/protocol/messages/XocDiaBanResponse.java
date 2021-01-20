package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class XocDiaBanResponse
        extends AbstractResponseMessage {

    public long moneyAdd;
    public long totalBetMoney;
    public String message;
    public int mCode;
    public int betSide;

    public void setSuccess(String _msg, int _betSide) {
        this.mCode = 1;
        this.betSide = _betSide;
        this.message = _msg;

    }

    public void setFailure(String msg) {
        this.mCode = 0;
        this.message = msg;
    }

    public IResponseMessage createNew() {
        return new XocDiaBanResponse();
    }
}
