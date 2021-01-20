package allinone.protocol.messages;

import java.util.List;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.UserEntity;

public class GetBestPlayerResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public List<UserEntity> mBestPlayerList;
    public int type;
    public String userName;
    public String viewname;
    public long rankData;
    public int ranking;
    public String avatarID;

    public void setSuccess(int aCode, List<UserEntity> aBestList) {
    	mCode = aCode;
        mBestPlayerList = aBestList;
    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew() {
        return new GetBestPlayerResponse();
    }
}
