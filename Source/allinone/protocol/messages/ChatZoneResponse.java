package allinone.protocol.messages;

import java.util.ArrayList;
import vn.game.entity.ChatZoneUtity;
import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import vn.game.session.ISession;

public class ChatZoneResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public String mMessage;
    public String mUsername;
    public long zoneid;

    public long uid;
    public int type;
    public ArrayList<ChatZoneUtity> listChat = null;
    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public void setSuccess(int aCode,String aMessage,int _type, String viewname ) {
        mCode = aCode;
        mMessage = aMessage;
        type = _type;
        mUsername = viewname;
    }
    public void setSuccessList(int aCode,ArrayList<ChatZoneUtity> _listChat) {
        mCode = aCode;
        listChat = _listChat;
    }
    public void setMessage(String aMessage) {
        mMessage = aMessage;
    }

    public void setUsername(String aUsername) {
        mUsername = aUsername;
    }

    public void setZoneId(long zoneid) {
        zoneid = zoneid;
    }

    public void setType(int aType) {
        type = aType;
    }

    public IResponseMessage createNew() {
        return new ChatZoneResponse();
    }

    @Override
    public IResponseMessage clone(ISession session) {
        ChatZoneResponse resMsg = (ChatZoneResponse) createNew();

        resMsg.session = session;
        resMsg.setID(this.getID());
        resMsg.mCode = mCode;
        resMsg.mErrorMsg = mErrorMsg;

        resMsg.mMessage = mMessage;
        resMsg.mUsername = mUsername;
        resMsg.zoneid = zoneid;

        resMsg.type = type;
        resMsg.uid = uid;
        resMsg.listChat = listChat;

        return resMsg;
    }

}
