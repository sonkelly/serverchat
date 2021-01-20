package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.UserEntity;

public class AddFriendByNameResponse extends AbstractResponseMessage
{

    public String mErrorMsg;
    public UserEntity user;
    public void setSuccess(int aCode, UserEntity u)
    {
        mCode = aCode;
        user = u;
    }

    public void setFailure(int aCode, String aErrorMsg)
    {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew()
    {
        return new AddFriendByNameResponse();
    }
}
