package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class RegisterResponse21 extends AbstractResponseMessage
{

    public String mErrorMsg;
    public long mUid;
    public long money;
    public int avatarID;
    public int level;
    public String values="";
    
    public void setFailure(int aCode, String aErrorMsg)
    {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public void setSuccess(int aCode, long aUid, long mn, int avatar, int lev)
    {
        mCode = aCode;
        mUid = aUid;
        money = mn;
        avatarID = avatar;
        level = lev;
        
    }

    public IResponseMessage createNew()
    {
        return new RegisterResponse21();
    }
}
