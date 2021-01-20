package allinone.protocol.messages;

import java.util.ArrayList;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.LogvascEntity;

public class LogVasResponse extends AbstractResponseMessage {

    public String errMgs;
    public ArrayList<LogvascEntity> data = new ArrayList<LogvascEntity>();
    public String value;

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        errMgs = aErrorMsg;
    }

    public void setSuccess(int aCode, ArrayList<LogvascEntity> data) {
        mCode = aCode;
        this.data = data;
    }

    @Override
    public IResponseMessage createNew() {
        return new LogVasResponse();
    }
}
