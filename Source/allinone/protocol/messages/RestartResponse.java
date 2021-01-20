package allinone.protocol.messages;

import java.util.ArrayList;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.SimplePlayer;
public class RestartResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public boolean isEmptyRoom;
    public SimplePlayer roomOwner;
    
    
    public long matchID;
    public String roomName;
    public int zoneID;
    public int available = 0; //cờ tướng: chấp
    public long begin_id; //cờ tướng: ID người thua được đi trước
    //Thomc

    public void setZoneID(int z) {
        zoneID = z;
    }


    public void setFailure(int aCode, String aErrorMsg, boolean isEmpty) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew() {
        return new RestartResponse();
    }
}
