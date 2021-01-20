package allinone.protocol.messages;

import java.util.Vector;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.UserEntity;

public class GetFreeFriendListResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public Vector<UserEntity> mFrientList = new Vector<UserEntity>();
    

    public void setSuccess(int aCode, Vector<UserEntity> aFrientList) {
        mCode = aCode;
        mFrientList = aFrientList;
    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew() {
        return new GetFreeFriendListResponse();
    }
}
