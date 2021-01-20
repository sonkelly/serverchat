package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class ChatRequest extends AbstractRequestMessage
{

    public long mRoomId;
    public String mMessage;

    public int type;
    public int phongID;
    public IRequestMessage createNew()
    {
        return new ChatRequest();
    }
}
