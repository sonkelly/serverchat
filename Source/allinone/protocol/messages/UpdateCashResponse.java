package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class UpdateCashResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public long mUid;
    public String mUsername;
    public int mAge;
    public boolean mIsMale;
    public int level;
    public long realmoney;
    public long money;
    public int playsNumber;
    public String AvatarID;
    public int experience;
    public boolean isActive;

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public void setSuccess(int aCode, long aUid, String aUsername,
            int aAge, boolean aIsMale, long _realmoney,long m, int pN, String aID, int l, int e, boolean active) {
        mCode = aCode;
        mUid = aUid;
        mUsername = aUsername;
        mAge = aAge;
        mIsMale = aIsMale;
        realmoney = _realmoney;
        money = m;
        playsNumber = pN;
        AvatarID = aID;
        level = l;
        experience = e;
        isActive = active;
    }

    public void setSuccessUpdateMoney(int aCode, long aUid, String aUsername, long _money,long _realmoney, boolean active) {
        mCode = aCode;
        mUid = aUid;
        mUsername = aUsername;
        realmoney = _realmoney;
        money = _money;
        isActive = active;
    }

    public IResponseMessage createNew() {
        return new UpdateCashResponse();
    }
}
