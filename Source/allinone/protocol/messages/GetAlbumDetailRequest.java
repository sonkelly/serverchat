package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetAlbumDetailRequest extends AbstractRequestMessage
{

    public long albumId;
    public int page;
    public int size;
    

    public IRequestMessage createNew()
    {
        return new GetAlbumDetailRequest();
    }

}
