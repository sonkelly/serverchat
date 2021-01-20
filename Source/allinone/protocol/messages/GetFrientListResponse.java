package allinone.protocol.messages;

import java.util.Vector;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.UserEntity;

public class GetFrientListResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    //public int mNumPlayingRoom;
    public Vector<UserEntity> mFrientList;

    public void setSuccess(int aCode, Vector<UserEntity> aFrientList) {
        mCode = aCode;
        // mNumPlayingRoom = aNumPlayingRoom;
        mFrientList = aFrientList;
    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew() {
        return new GetFrientListResponse();
    }
}
