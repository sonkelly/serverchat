package allinone.protocol.messages;

import java.util.ArrayList;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import vn.game.session.ISession;

public class ReadyResponse extends AbstractResponseMessage {
	public String mErrorMsg;
    public long mUid;
    public boolean ready;
    public int zone;
    public int startCount = 0;
    
    public void setSuccess(int aCode, long aUid, boolean r)
    {
        mCode = aCode;
        mUid = aUid;
        ready = r;
    }


    public void setFailure(int aCode, String aErrorMsg)
    {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew()
    {
        return new ReadyResponse();
    }
    
    public ReadyResponse clone(ISession session)
      {
          	ReadyResponse resMsg = (ReadyResponse)createNew();
            resMsg.mUid = mUid;
            resMsg.session = session;
            resMsg.setID(this.getID());
            resMsg.mCode = mCode;
            resMsg.ready = ready;
            resMsg.zone = zone;
            resMsg.startCount = startCount;
            
            return resMsg;
      }
}
