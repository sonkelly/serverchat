package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class LogAdmobRequest extends AbstractRequestMessage
{

  
    public int typeAds; //1 banner, 2 video
    public int isComplete;//1 hoan thanh 0, chua hoan thanh video
    public IRequestMessage createNew()
    {
        return new LogAdmobRequest();
    }
}
