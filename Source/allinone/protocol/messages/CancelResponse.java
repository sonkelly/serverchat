/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import vn.game.room.Zone;
import vn.game.session.ISession;

/**
 *
 * @author binh_lethanh
 */
public class CancelResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public long uid;
    public boolean isGamePlaying;
    public boolean isUserPlaying;
    public String message;
    public long newOwner = 0;
    public long ownerMoney;
    public long playerMoney;
    public long next_id = -1;
    public boolean isNewRound = false;
    public int zone_id;
    public int phongId;
    public Zone zone;
    public int cancelStatus = 0;

    public void setZone(int id) {
        zone_id = id;
    }

    public void setNextPlayer(long id, boolean isNewRound_) {
        this.next_id = id;
        this.isNewRound = isNewRound_;
    }

    public void setSuccess(int aCode, long id) {
        mCode = aCode;
        uid = id;
    }

    public void setSuccessOwner(int aCode, long id, long Ownerid) {
        mCode = aCode;
        uid = id;
        newOwner = Ownerid;
    }

    public void setUid(long id) {
        uid = id;
    }

    public void setMoneyEndMatch(long ownerMoney_, long playerMoney_) {
        ownerMoney = ownerMoney_;
        playerMoney = playerMoney_;
    }

    public void setUserPlaying(boolean play) {
        this.isUserPlaying = play;
    }

    public void setGamePlaying(boolean play) {
        this.isGamePlaying = play;
    }

    public void setMessage(String m) {
        message = m;
    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew() {
        return new CancelResponse();
    }

    public IResponseMessage clone(ISession session) {
        CancelResponse resMsg = (CancelResponse) createNew();
        resMsg.message = message;
        resMsg.session = session;
        resMsg.setID(this.getID());
        resMsg.mCode = mCode;

        resMsg.uid = uid;

        resMsg.isGamePlaying = isGamePlaying;
        resMsg.isUserPlaying = isUserPlaying;

        resMsg.newOwner = newOwner;
        //Thomc: for co tuong
        resMsg.ownerMoney = ownerMoney;
        resMsg.playerMoney = playerMoney;
        //Thomc
        //trường hợp đang đến lượt đi mà thoát game  thì chuyển lượt và newRound cho những người còn lại
        resMsg.next_id = next_id;
        resMsg.isNewRound = isNewRound;
        resMsg.zone_id = zone_id;
        resMsg.phongId = phongId;
        resMsg.zone = null;
        resMsg.mErrorMsg = mErrorMsg;
        resMsg.cancelStatus = cancelStatus;
        return resMsg;
    }
}
