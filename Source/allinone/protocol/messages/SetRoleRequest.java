package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class SetRoleRequest extends AbstractRequestMessage
{
    public int systemObjectId;
    public long systemObjectRecordId;
    public int roleId;
    public IRequestMessage createNew()
    {
        return new SetRoleRequest();
    }
}
