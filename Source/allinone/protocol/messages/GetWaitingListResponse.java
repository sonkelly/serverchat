package allinone.protocol.messages;

import java.util.Vector;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import vn.game.room.RoomEntity;

public class GetWaitingListResponse extends AbstractResponseMessage
{

    public String mErrorMsg;
    public int mNumPlayingRoom;
    public Vector<RoomEntity> mWaitingRooms;
    public int zoneID;
    public void setSuccess(int aCode, int aNumPlayingRoom, 
    		Vector<RoomEntity> aWaitingRooms, int zone)
    {
        mCode = aCode;
        mNumPlayingRoom = aNumPlayingRoom;
        mWaitingRooms = aWaitingRooms;
        zoneID = zone;
    }

    public void setFailure(int aCode, String aErrorMsg)
    {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew()
    {
        return new GetWaitingListResponse();
    }

}
