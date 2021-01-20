package allinone.protocol.messages;

import java.util.List;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.room.NRoomEntity;
import java.util.ArrayList;

public class ListPhongResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    
    public String value;
    public ArrayList<Integer> listPhong ;
    
    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public void setSuccess(int aCode) {
        mCode = aCode;
    }

    public IResponseMessage createNew() {
        return new ListPhongResponse();
    }
}
