package allinone.protocol.messages;

import allinone.common.Position;
import java.util.List;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;
import allinone.data.SimplePlayer;

public class ReconnectResponse extends AbstractResponseMessage {

    public String mErrorMsg;

    public int currCard;

    public List<? extends SimplePlayer> mLiengPlayer;
    public List<? extends SimplePlayer> mWaitingPlayerLieng;

    public List<? extends SimplePlayer> mTienLenPlayer;
    public List<? extends SimplePlayer> mWaitingPlayerTienlen;
    public List<? extends SimplePlayer> playing;
    public List<? extends SimplePlayer> waiting;

    public SimplePlayer roomOwner;
    //public OTTPlayer roomOwnerOTT;
    public String roomName;
    public long mMatchId;
    public long minBet;
    public int zoneID;
    public int capacity;
    public boolean isAn;
    public boolean isTaiGui;
    public boolean isPlaying;
    public boolean isResume;
    public boolean isObserve;
    public long turn;
    public int deck;
    public String cards = "";
    public String myHandCards;
    public boolean isInvite = false;

    //Caro
    public boolean mIsStarting;
    public boolean mIsPlayer;
    public boolean mIsYourTurn;
    public int mType;

    public boolean isJoinAfterPlaying = false;
    public int available = 0;
    public long currentID = 0;
    public boolean truong;
    public boolean isNeeded = true;

    //tien len
    public boolean isHidePoker = true;
    public int duty;
    public long currUserHasDuty;

    //Binh
    public long remainTime;
    public int binhStatus;

    // 3 cay
    public boolean isChiaBai;
    public long ownerId;

    // sam
    public long baoSamId;
    public boolean isSentBaoSam;

    // lieng
    public long totalCall;

    // xito
    public String betShowAllMoney;

    // poker
    public String communityCards;
    public long dealId;

    //xocdia
    public int state;
    public long numWhite = 0L;
    public long numBlack = 0L;
    public boolean isChan = false;

    public long moneyPlayLe = 0L;

    public long moneyPlayChan = 0L;

    public long moneyFourWhite = 0L;

    public long moneyFourBlack = 0L;

    public long moneyThreeWhiteOneBlack = 0L;

    public long moneyThreeBlackOneWhite = 0L;
    public boolean is4Trang = false;
    public boolean is4Den = false;
    public boolean is3Trang1Den = false;
    public boolean is3Den1Trang = false;
    public boolean isBanCuaChan = false;
    public boolean isBanCuaLe = false;
    public long reconnectUid;

    //  HeadBall has 1 properties: headBallPosition 
    public Position headBallPosition;

    public long leftTime = 0;

    public long timeInTable = 0;
    public Position currentPositionCatch;
    public Position currentPositionFoot;
    
    public boolean sentStatusThree;

    public void setIsJoinAfterPlaying(boolean isTrue) {
        this.isJoinAfterPlaying = isTrue;
    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public void setOwner(SimplePlayer owner) {
        this.roomOwner = owner;
    }

    public void setCapacity(int c) {
        capacity = c;
    }

    public void setPhomInfo(boolean isAn, boolean isTaiGui, boolean isPlaying, boolean isResume, long turn, String cards, int deck) {
        this.isAn = isAn;
        this.isTaiGui = isTaiGui;
        this.isPlaying = isPlaying;
        this.isResume = isResume;
        this.turn = turn;
        this.cards = cards;
        this.deck = deck;
    }

    public void setSuccess() {
        mCode = ResponseCode.SUCCESS;
    }

    public void setSuccess(int aCode, String rName, long bet, int zone) {
        zoneID = zone;
        mCode = aCode;
        roomName = rName;
        minBet = bet;
    }

    public void setRoomID(long aRoomId) {
        mMatchId = aRoomId;
    }

    public void setSuccess(int aCode, boolean aIsPlayer, boolean aIsStarting) {
        mCode = aCode;
        mIsPlayer = aIsPlayer;
        mIsStarting = aIsStarting;
    }

    public void setValue(boolean aIsYourTurn, int aType) {
        mIsYourTurn = aIsYourTurn;
        mType = aType;
    }

    public void setCurrentPlayersTienLen(List<? extends SimplePlayer> aValues, List<? extends SimplePlayer> bValues,
            SimplePlayer owner) {
        roomOwner = owner;
        mTienLenPlayer = aValues;
        mWaitingPlayerTienlen = bValues;
    }

    public void setCurrentPlayersLieng(List<? extends SimplePlayer> aValues, List<? extends SimplePlayer> bValues,
            SimplePlayer owner) {
        roomOwner = owner;
        mLiengPlayer = aValues;
        mWaitingPlayerLieng = bValues;
    }


    public void setCurrentPlayers(List<? extends SimplePlayer> playing,
            List<? extends SimplePlayer> waiting, SimplePlayer owner) {
        roomOwner = owner;
        this.playing = playing;
        this.waiting = waiting;
    }

//    public void setCurrentPlayersXocDia(List<XocDiaPlayer> playings, SimplePlayer owner) {
//        roomOwner = owner;
//        mXocDiaPlayers = playings;
//        
//    }
    public void setCurrentPlayersXocDia(List<? extends SimplePlayer> playing,
            List<? extends SimplePlayer> waiting, SimplePlayer owner, int _state, long _reconnectUid) {

        roomOwner = owner;
        this.playing = playing;
        this.waiting = waiting;
        this.state = _state;
        this.reconnectUid = _reconnectUid;
    }

    public void setCurrentFootBall(List<? extends SimplePlayer> playing,
            List<? extends SimplePlayer> waiting, SimplePlayer owner, long _reconnectUid) {
        roomOwner = owner;
        this.playing = playing;
        this.waiting = waiting;
        this.reconnectUid = _reconnectUid;
    }


    public void setCurrentHeadBall(List<? extends SimplePlayer> playing,
            List<? extends SimplePlayer> waiting, SimplePlayer owner, long _reconnectUid, Position headBallPosition) {
        roomOwner = owner;
        this.playing = playing;
        this.waiting = waiting;
        this.reconnectUid = _reconnectUid;
        this.headBallPosition = headBallPosition;
    }

    public void setBidaInfo(long _turn, long _timeleft) {
        this.turn = _turn;
        this.remainTime = _timeleft;

    }

    @Override
    public IResponseMessage createNew() {
        return new ReconnectResponse();
    }
}
