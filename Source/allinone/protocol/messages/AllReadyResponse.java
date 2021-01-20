package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class AllReadyResponse extends AbstractResponseMessage {

    public void setSuccess(int aCode)
    {
        mCode = aCode;
    }

    public IResponseMessage createNew()
    {
        return new AllReadyResponse();
    }
}
