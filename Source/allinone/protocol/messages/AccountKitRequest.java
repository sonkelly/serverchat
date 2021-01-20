package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class AccountKitRequest extends AbstractRequestMessage
{

  
    public String phone;
    public IRequestMessage createNew()
    {
        return new AccountKitRequest();
    }
}
