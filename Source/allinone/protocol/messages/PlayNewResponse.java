/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

/**
 *
 * @author binh_lethanh
 */
public class PlayNewResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public long mMatchId;
    public long uid;
    public long minBet;
    public int capacity;
    public long ownerCash;
    public boolean join;
    //cờ tướng
    public int available = 0;//chấp quân gì
    
    public boolean isHidePoker;

    public void setSuccess(int aCode, long aMatchId, long id, long bet, int cap) {
        mCode = aCode;
        mMatchId = aMatchId;
        uid = id;
        minBet = bet;
        capacity = cap;
    }

    public void setCash(long cash) {
        ownerCash = cash;
    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }
    
    public IResponseMessage createNew() {
        return new PlayNewResponse();
    }
}
