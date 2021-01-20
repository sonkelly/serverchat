package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class ChangeMoneyTypeRequest extends AbstractRequestMessage
{
    public int moneType;//0 tien ao, 1 tien that
    
    @Override
    public IRequestMessage createNew()
    {
        return new ChangeMoneyTypeRequest();
    }
}
