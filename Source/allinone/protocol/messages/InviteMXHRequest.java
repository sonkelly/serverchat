package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class InviteMXHRequest extends AbstractRequestMessage
{
 
    public int gameId;
    public long destUid;
    public long betMoney;


    public IRequestMessage createNew()
    {
        return new InviteMXHRequest();
    }
}
