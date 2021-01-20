package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class XocDiaFireResponse
        extends AbstractResponseMessage {

    public long moneyCurrent = 0L;
    public long moneyAdd = 0L;
    public long totalBetMoney = 0L;
    public String message;
    public int mCode;
    public int isbetside = 0;

    public void setSuccess(long _money, long _moneyCurrent, int isbetside, long _totalBetMoney) {
        this.mCode = 1;
        switch (isbetside) {
            case 0:
                this.message = ("Bạn đã đặt lẻ số tiền " + _money + " thành công");
                break;
            case 1:
                this.message = ("Bạn đã đặt chẵn số tiền " + _money + " thành công");
                break;
            case 2:
                this.message = ("Bạn đã đặt x1 số tiền " + _money + " thành công");
                break;
            case 3:
                this.message = ("Bạn đã đặt x2 số tiền " + _money + " thành công");
                break;
            case 4:
                this.message = ("Bạn đã đặt x3 số tiền " + _money + " thành công");
                break;
            case 5:
                this.message = ("Bạn đã đặt x4 số tiền " + _money + " thành công");
                break;

        }
        this.moneyCurrent = _moneyCurrent;
        this.totalBetMoney = _totalBetMoney;
        this.isbetside = isbetside;
        this.moneyAdd = _money;
    }

    public void setFailure(String msg) {
        this.mCode = 0;
        this.message = msg;
    }

    public IResponseMessage createNew() {
        return new XocDiaFireResponse();
    }
}
