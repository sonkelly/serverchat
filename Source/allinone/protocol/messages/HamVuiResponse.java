/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

import java.util.ArrayList;
import java.util.List;

import allinone.data.ResponseCode;
import allinone.data.SimplePlayer;
//import bacay.data.BacayPlayer;
import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

/**
 *
 * @author binh_lethanh
 */
 public class HamVuiResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public int currCard;
 
    
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
    public int available = 0; //cờ tướng: quân cờ chấp
    public long currentID = 0;
    public boolean truong;
    public boolean isNeeded = true;
    
    //tien len
    public boolean isHidePoker = true;
    public int duty;

    public void setIsJoinAfterPlaying(boolean isTrue) {
        this.isJoinAfterPlaying = isTrue;
    }
//Tho :end

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
    public void setSuccess(){
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

    
    
    public void setCurrentPlayers(List<? extends SimplePlayer> playing, 
                List<? extends SimplePlayer> waiting, SimplePlayer owner)
    {   
        roomOwner = owner;
        this.playing = playing;
        this.waiting = waiting;
    }

    public IResponseMessage createNew() {
        return new HamVuiResponse();
    }
}
