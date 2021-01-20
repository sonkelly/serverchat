package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import vn.game.session.ISession;

public class OutResponse extends AbstractResponseMessage {

    public long mUid;
    public String message;
    public String username;
    public long newRoomOwner = 0;
    public int out;
    public int type; // for auto kick out
    
    public void setSuccess(int aCode, long aUid, String m, String u, int o) {
        mCode = aCode;
        mUid = aUid;
        message = m;
        username = u;
        out = o;
    }

    public void setNewRoomOwner(long newOwner) {
        newRoomOwner = newOwner;
    }

    public IResponseMessage createNew() {
        return new OutResponse();
    }
    
    @Override
     public IResponseMessage clone(ISession session)
     {
          OutResponse resMsg = (OutResponse)createNew();

            resMsg.session = session;
            resMsg.setID(this.getID());
            resMsg.mCode = mCode;
            resMsg.message = message;
            resMsg.mUid = mUid;
            
            resMsg.username = username;
            resMsg.newRoomOwner = newRoomOwner;
            resMsg.out = out;
            resMsg.type = type; 
            
            return resMsg;
            
     }
    
}
