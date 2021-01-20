package allinone.protocol.messages;

import java.util.List;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.room.NRoomEntity;

public class EnterZoneResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public int timeout;
    public List<NRoomEntity> lstRooms;
    public String value;
    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public void setSuccess(int aCode) {
        mCode = aCode;
    }

    public IResponseMessage createNew() {
        return new EnterZoneResponse();
    }
}
