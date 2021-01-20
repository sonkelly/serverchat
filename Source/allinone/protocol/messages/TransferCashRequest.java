package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class TransferCashRequest extends AbstractRequestMessage
{
    public long money;
    public long source_uid;
    //public long desc_uid;
    public String destName;
    public IRequestMessage createNew()
    {
        return new TransferCashRequest();
    }
}
