package allinone.protocol.messages;

import java.util.ArrayList;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.Mail;

public class MailResponse extends AbstractResponseMessage {

    public String errMgs;
    public ArrayList<Mail> mess = new ArrayList<Mail>();
    public String value; 
    
    public IResponseMessage createNew() {
        return new MailResponse();
    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        errMgs = aErrorMsg;
    }
    public void setSuccess(int aCode, ArrayList<Mail> me) {
        mCode = aCode;
        this.mess = me;
    }
}
