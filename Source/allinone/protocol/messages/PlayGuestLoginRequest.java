package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;


public class PlayGuestLoginRequest extends AbstractRequestMessage
{
    public int partnerId  	= 0;
    public int refCode 		= 0;
    public String deviceUId = "";    
    public String mobileVersion = "";
    public int deviceType = 4;
        
    public IRequestMessage createNew()
    {
        return new PlayGuestLoginRequest();
    }

}
