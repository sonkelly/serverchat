package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class VivuAppearResponse extends AbstractResponseMessage {

    public String errMgs;
    public String name;
    public String attr;
    public long uid;
    public int xPos;
    public int yPos;
    @Override
    public IResponseMessage createNew() {
        return new VivuAppearResponse();
    }

    public void setFailure(String aErrorMsg) {
        mCode = ResponseCode.FAILURE;
        errMgs = aErrorMsg;
    }

    public void setSuccess( long id, String n, String a, int x, int y) {
        mCode = ResponseCode.SUCCESS;
        xPos = x;
        yPos = y;
        this.uid = id;
        this.name = n;
        this.attr = a;
    }
}
