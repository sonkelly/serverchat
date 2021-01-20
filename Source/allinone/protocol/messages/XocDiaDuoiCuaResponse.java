package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class XocDiaDuoiCuaResponse
        extends AbstractResponseMessage {

    public long moneyAdd;
    public long totalBetMoney;
    public String message;
    public int mCode;
    public boolean isOwner;

    public void setSuccess(String _msg, boolean _isOwner) {
        this.mCode = 1;
        this.isOwner = _isOwner;
        this.message = _msg;

    }

    public void setFailure(String msg) {
        this.mCode = 0;
        this.message = msg;
    }

    public IResponseMessage createNew() {
        return new XocDiaDuoiCuaResponse();
    }
}
