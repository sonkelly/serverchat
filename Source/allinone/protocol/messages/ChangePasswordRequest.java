package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class ChangePasswordRequest extends AbstractRequestMessage
{
    public String oldPassword = "";    
    public String newPassword = "";
    public String reTypePassword = "";
    
    @Override
    public IRequestMessage createNew()
    {
        return new ChangePasswordRequest();
    }
    
}
