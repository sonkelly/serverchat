package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class QuayCNKDResponse extends AbstractResponseMessage
{

    public String errMessage;
    public String msg;
    public String moneyWin="";
    public String count=""; 	
    public void setFailure(String aErrorMsg)
    {
        mCode = ResponseCode.FAILURE;
        errMessage = aErrorMsg;
    }
    

    public IResponseMessage createNew()
    {
        return new QuayCNKDResponse();
    }
}
