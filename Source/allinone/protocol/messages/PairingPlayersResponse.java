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
public class PairingPlayersResponse extends AbstractResponseMessage {

    public int zoneId;
    public String mErrorMsg;
//    public long mMatchId;
    public long uid;
    public long minBet;
    public String message;
//    public boolean join;
//    public String index;
//    
//    public long moneyBet;
//    public int roomType;
//    public String password;
//    public int size;
//    public int roomLevel = 0;

//    public void setSuccess(int aCode, long aMatchId, long id, long bet, int cap) {
//        mCode = aCode;
//        mMatchId = aMatchId;
//        uid = id;
//        minBet = bet;
//        capacity = cap;
//    }
//
//    public String getIndex() {
//        return index;
//    }
//
//    public void setIndex(String index) {
//        this.index = index;
//    }
//
//    public void setCash(long cash) {
//        ownerCash = cash;
//    }
    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew() {
        return new PairingPlayersResponse();
    }
}
