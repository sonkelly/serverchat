package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class BaoBinhResponse extends AbstractResponseMessage {
	
	public String message;
	public long uid;
    public boolean hasBao;
    public int binhType;
    public int times;

    public void setSuccess(long u, boolean hasBao)
    {
        mCode = ResponseCode.SUCCESS;
        uid = u;
        this.hasBao = hasBao;
    }
    
    public void setFailure(String msg){
        mCode = ResponseCode.FAILURE;
        message = msg;
    }
    
    public IResponseMessage createNew()
    {
        return new BaoBinhResponse();
    }
}
