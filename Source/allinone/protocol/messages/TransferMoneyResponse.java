/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

/**
 *
 * @author Zeple
 */
import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class TransferMoneyResponse
        extends AbstractResponseMessage {

    public String mErrorMsg;
    public long curMoney;

    public void success(String mess, long _curMoney) {
        this.mCode = 1;
        this.mErrorMsg = mess;
        this.curMoney = _curMoney;
    }

    public void setFailure(String mess) {
        this.mCode = 0;
        this.mErrorMsg = mess;
    }

    public IResponseMessage createNew() {
        return new TransferMoneyResponse();
    }
}
