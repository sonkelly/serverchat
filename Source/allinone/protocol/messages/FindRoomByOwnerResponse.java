package allinone.protocol.messages;


import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import vn.game.room.RoomEntity;

public class FindRoomByOwnerResponse extends AbstractResponseMessage
{

    public String mErrorMsg;
    public RoomEntity mRoom;

    public void setSuccess(int aCode, RoomEntity aRoom)
    {
        mCode = aCode;
        mRoom = aRoom;
    }

    public void setFailure(int aCode, String aErrorMsg)
    {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew()
    {
        return new FindRoomByOwnerResponse();
    }
}
