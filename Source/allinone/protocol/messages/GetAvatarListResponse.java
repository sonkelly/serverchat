package allinone.protocol.messages;

import java.util.Vector;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AvatarEntity;
import java.util.ArrayList;

public class GetAvatarListResponse extends AbstractResponseMessage {

    public String mErrorMsg;

    public ArrayList<AvatarEntity> mAvatarList;

    public void setSuccess(int aCode, ArrayList<AvatarEntity> aAvatarList) {
        mCode = aCode;
        mAvatarList = aAvatarList;
    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    @Override
    public IResponseMessage createNew() {
        return new GetAvatarListResponse();
    }
}
