package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class XocDiaOwnerCanCuaResponse
        extends AbstractResponseMessage {

    public long moneyAdd;
    public long totalBetMoney;
    public String message;
    public int mCode;
    public boolean isOwnerCanCua;

    public void setSuccess( String _msg, boolean _isOwnerCanCua) {
        this.mCode = 1;
        this.isOwnerCanCua = _isOwnerCanCua;
        this.message = _msg;

    }

    public void setFailure(String msg) {
        this.mCode = 0;
        this.message = msg;
    }

    public IResponseMessage createNew() {
        return new XocDiaOwnerCanCuaResponse();
    }
}
