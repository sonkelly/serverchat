package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetImageRequest extends AbstractRequestMessage
{

    public String name;
    public IRequestMessage createNew()
    {
        return new GetImageRequest();
    }
}
