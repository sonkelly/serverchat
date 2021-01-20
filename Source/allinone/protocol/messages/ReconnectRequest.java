package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class ReconnectRequest extends AbstractRequestMessage {
    
	public long matchId;
    public long uid;
    public int zone;
    public int phong;
    public String username;
    public String pass;
    public int type;
    public int tourID;
    public boolean isMxh;
    public int protocol;
    public int versionCode = 0;
    
    public IRequestMessage createNew()
    {
        return new ReconnectRequest();
    }
}
