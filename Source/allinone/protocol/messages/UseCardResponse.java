package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class UseCardResponse extends AbstractResponseMessage {

    public String message;

    public void setResponse(int aCode, String message) {
        mCode = aCode;
        this.message = message;
    }

    public IResponseMessage createNew() {
        return new UseCardResponse();
    }
}
