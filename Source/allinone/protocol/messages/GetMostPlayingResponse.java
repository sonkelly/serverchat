package allinone.protocol.messages;

import java.util.Vector;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.UserEntity;

public class GetMostPlayingResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public Vector<UserEntity> mMostPlayingist;

    public void setSuccess(int aCode, Vector<UserEntity> aMostPlayingList) {
        mCode = aCode;
        mMostPlayingist = aMostPlayingList;
    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew() {
        return new GetMostPlayingResponse();
    }
}
