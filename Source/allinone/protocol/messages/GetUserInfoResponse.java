package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class GetUserInfoResponse extends AbstractResponseMessage
{

    public String mErrorMsg;
    public long mUid;
    public String mUsername;
    public String vipName;
    public int mAge;
    public boolean mIsMale;
    public int level;

    public long money;
    public int playsNumber;
    public String AvatarID;
    public boolean isFriend;
    public int experience;
    
    public void setFailure(int aCode, String aErrorMsg)
    {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public void setSuccess(int aCode, long aUid, String aUsername, 
    		int aAge, boolean aIsMale, long m, int pN, String aID, boolean is, int l, int e)
    {
        mCode = aCode;
        mUid = aUid;
        mUsername = aUsername;
        mAge = aAge;
        mIsMale = aIsMale;
        money = m;
        playsNumber = pN;
        AvatarID = aID;
        isFriend = is;
        level = l;
        experience = e;
    }

    public IResponseMessage createNew()
    {
        return new GetUserInfoResponse();
    }

}
