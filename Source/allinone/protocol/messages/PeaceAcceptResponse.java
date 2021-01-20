/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

/**
 *
 * @author Dinhpv
 */
public class PeaceAcceptResponse extends AbstractResponseMessage {

    public String message;
    public long ownerMoney;
    public long playerMoney;

    public void setSuccess(int aCode) {
        mCode = aCode;
    }

    public void setFailure(int aCode, String msg) {
        mCode = aCode;
        message = msg;
    }

    public void setMoneyEndMatch(long ownerMoney_, long playerMoney_) {
        ownerMoney = ownerMoney_;
        playerMoney = playerMoney_;
    }

    public IResponseMessage createNew() {
        return new PeaceAcceptResponse();
    }
}
