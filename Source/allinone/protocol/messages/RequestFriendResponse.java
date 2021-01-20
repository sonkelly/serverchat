package allinone.protocol.messages;



import allinone.data.UserEntity;
import java.util.Vector;
import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class RequestFriendResponse extends AbstractResponseMessage
{
    public String value;
    public String mErrorMsg;
//    
//    public void setSuccess(int aCode)
//    {
//        mCode = aCode;
//    }

    public Vector<UserEntity> mFrientList;

    public void setSuccess(int aCode, Vector<UserEntity> aFrientList) {
        mCode = aCode;
        // mNumPlayingRoom = aNumPlayingRoom;
        mFrientList = aFrientList;
    }
    public void setFailure(int aCode, String aErrorMsg)
    {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew()
    {
        return new RequestFriendResponse();
    }
}
