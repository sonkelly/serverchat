package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class ViewNameResponse
        extends AbstractResponseMessage {

  
    public String message;
    public int mCode;
    public String viewname;

    public void setSuccess( String _msg, String viewname) {
        this.mCode = 1;
       
        this.message = _msg;
        this.viewname = viewname;

    }

    public void setFailure(String msg) {
        this.mCode = 0;
        this.message = msg;
    }

    public IResponseMessage createNew() {
        return new ViewNameResponse();
    }
}
