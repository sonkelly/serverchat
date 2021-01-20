package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class XocDiaMuaResponse
        extends AbstractResponseMessage {

    public long moneyAdd;
    public long totalBetMoney;
    public String message;
    public int mCode;
    public int betSide;
    public long uidBuy;

    public void setSuccess(String _msg, int _betSide,long _uidBuy) {
        this.mCode = 1;
        this.betSide = _betSide;
        this.message = _msg;
        this.uidBuy = _uidBuy;

    }

    public void setFailure(String msg) {
        this.mCode = 0;
        this.message = msg;
    }

    @Override
    public IResponseMessage createNew() {
        return new XocDiaMuaResponse();
    }
}
