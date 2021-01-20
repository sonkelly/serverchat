package allinone.protocol.messages;


import java.util.List;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.UserEntity;

public class TopEventResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public List<UserEntity> players;
    public String title;
    public String fromDate;
    public String toDate;
    
    public void setSuccess(int aCode, List<UserEntity> aBestList) {
        mCode = aCode;
        players = aBestList;
    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew() {
        return new TopEventResponse();
    }
}
