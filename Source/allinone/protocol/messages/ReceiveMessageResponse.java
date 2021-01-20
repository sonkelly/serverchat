package allinone.protocol.messages;

import java.util.ArrayList;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.Message;

public class ReceiveMessageResponse extends AbstractResponseMessage {

    public String errMgs;
    public ArrayList<Message> mess = new ArrayList<Message>();
    public String value; //for  mxh
    
    public IResponseMessage createNew() {
        return new ReceiveMessageResponse();
    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        errMgs = aErrorMsg;
    }
    public void setSuccess(int aCode, ArrayList<Message> me) {
        mCode = aCode;
        this.mess = me;
    }
}
