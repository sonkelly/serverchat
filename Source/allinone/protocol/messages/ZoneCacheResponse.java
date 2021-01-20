package allinone.protocol.messages;


import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class ZoneCacheResponse extends AbstractResponseMessage {

    public String message;
    public String gameInfo;
    public String active;
    public void setSuccess( String adv) {
        mCode = ResponseCode.SUCCESS;
        this.gameInfo = adv;
    }

    public ZoneCacheResponse() {
    }

    

    public void setFailure(int aCode, String msg) {
        mCode = aCode;
        message = msg;
    }

    public IResponseMessage createNew() {
        return new ZoneCacheResponse();
    }
}
