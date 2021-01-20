package allinone.protocol.messages;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
public class EndMatchResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public long mMatchId;
    public long roomOwnerID;
    public int zoneID;
    //Bacay


    public int uType;//0: Khong u, 1: u bt, 2: u khan, 3: u den
    public long phomDuty = 0;
    //OTT

    public boolean isFinalFight;
    //Co Tuong
    public long idWin;
    public long playerMoney;
    public long newOwner = 0;
    public boolean isPeace = false;
    //Thomc: Tienlen
    public ArrayList<String[]> tienlenResult;
    public long perfectType = 0;// Hết ván vì tới trắng!
    public String lastCards; //quân bài(s)  cuối cùng được đánh ra
    public ArrayList<long[]> fightInfo;// gửi về khi xảy ra chặt heo/hàng
    public long uid;// khi còn 2 người chơi mà uid thoát ra sẽ endgame nên phải gửi uid về
    public boolean checkEndWithDuty = false;
    public int numWhite;

    //Game New bacay
    public JSONObject endJson;

    public String value;

    public double power;
    public double angle;
    public long moneyLost;
    public long moneyWin;
    //add by zep dung cho an hu
    public long isUserAnHu = 0;
    public long moneyAnHu = 0;

    public void setZoneID(int z) {
        zoneID = z;
    }
//Thomc

    public void setLastCards(String cards) {
        this.lastCards = cards;
    }
//chặt heo/hàng phải gửi thông tin $ về room

    public void setFightInfo(ArrayList<long[]> fightInfo_) {
        this.fightInfo = fightInfo_;
    }

    public void setNewRoomOwner(long id) {
        this.roomOwnerID = id;
    }
//Co tuong

    public void setSuccess(int aCode, long idwin_, long aMatchId) {
        mMatchId = aMatchId;
        mCode = aCode;
        idWin = idwin_;

    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }



    public void setMoneyWinLost(long l, long w) {
        this.moneyLost = l;
        this.moneyWin = w;
    }

   

    public void setSuccess(JSONObject endJson) {
        this.endJson = endJson;
        mCode = ResponseCode.SUCCESS;
    }

    public void setSuccess(String value) {
        this.value = value;
        mCode = ResponseCode.SUCCESS;
    }

    public IResponseMessage createNew() {
        return new EndMatchResponse();
    }

    public IResponseMessage clone(ISession session) {
        EndMatchResponse resMsg = (EndMatchResponse) createNew();
        resMsg.mMatchId = mMatchId;
        resMsg.session = session;
        resMsg.setID(this.getID());
        resMsg.mCode = mCode;

        resMsg.roomOwnerID = roomOwnerID;
        resMsg.zoneID = zoneID;
        //Bacay

        //OTT

        resMsg.isFinalFight = isFinalFight;
        //Co Tuong
        resMsg.idWin = idWin;
        resMsg.playerMoney = playerMoney;
        resMsg.newOwner = newOwner;

        //Thomc: Tienlen
        resMsg.tienlenResult = tienlenResult;
        resMsg.perfectType = perfectType;// Hết ván vì tới trắng!
        resMsg.lastCards = lastCards; //quân bài(s)  cuối cùng được đánh ra
        resMsg.fightInfo = fightInfo;// gửi về khi xảy ra chặt heo/hàng
        resMsg.uid = uid;// khi còn 2 người chơi mà uid thoát ra sẽ endgame nên phải gửi uid về
        resMsg.checkEndWithDuty = checkEndWithDuty;

        resMsg.numWhite = numWhite;
//    
//    
//    //Game New bacay
        resMsg.endJson = endJson;

        resMsg.value = value;
        resMsg.isUserAnHu = isUserAnHu;
        resMsg.moneyAnHu = moneyAnHu;
        return resMsg;
    }

}
