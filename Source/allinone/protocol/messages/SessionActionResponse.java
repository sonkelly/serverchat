package allinone.protocol.messages;


import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import vn.game.session.ISession;

public class SessionActionResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public int zoneID;//game id
    public int roomID;
    public int status;
    
    public void setSuccess(int aCode) {
        mCode = aCode;
    }


    @Override
    public IResponseMessage createNew() {
        return new SessionActionResponse();
    }

    @Override
    public IResponseMessage clone(ISession session) {
        SessionActionResponse resMsg = (SessionActionResponse) createNew();

        resMsg.session = session;
        resMsg.setID(this.getID());
        resMsg.mCode = mCode;
        resMsg.mErrorMsg = mErrorMsg;
        resMsg.zoneID = zoneID;
        resMsg.roomID = roomID;
        resMsg.status = status;
        

        return resMsg;
    }

}
