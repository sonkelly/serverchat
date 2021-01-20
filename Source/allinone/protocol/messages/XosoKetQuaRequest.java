package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class XosoKetQuaRequest extends AbstractRequestMessage {

    public int idProvince;
    public int ngayLui;
    public IRequestMessage createNew()
    {
        return new XosoKetQuaRequest();
    }
}
