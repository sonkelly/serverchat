package allinone.protocol.messages;

import java.util.Vector;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.UserEntity;

public class GetRichestsResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public Vector<UserEntity> mRichestsList;

    public void setSuccess(int aCode, Vector<UserEntity> aRichestList) {
        mCode = aCode;
        mRichestsList = aRichestList;
    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew() {
        return new GetRichestsResponse();
    }
}
