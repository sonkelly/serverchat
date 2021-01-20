package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class XosoChoiRequest extends AbstractRequestMessage {

    public int idProvince;
    public int number;
    public long money;
    public int type;
    public IRequestMessage createNew()
    {
        return new XosoChoiRequest();
    }
}
