package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class BuyItemResponse extends AbstractResponseMessage
{

	public String errMessage;
	
	
    public void setFailure(String aErrorMsg)
    {
        mCode = ResponseCode.FAILURE;
        errMessage = aErrorMsg;
    }
    

    public IResponseMessage createNew()
    {
        return new BuyItemResponse();
    }
}
