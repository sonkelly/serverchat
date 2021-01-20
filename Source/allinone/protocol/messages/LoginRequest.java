package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class LoginRequest extends AbstractRequestMessage {

    public String mUsername;
    public String mPassword;
    public String mobileVersion = "";
    public String flashVersion = "";
    public String screen = "";
    public String device = "";
    public String cp = "0";
    public int partnerId = 0;
    public String refCode = "0";
    public int gamePosition;
    public boolean isMxh;
    public String deviceId = "";

    public boolean ver35 = false;

    //public String verID;
    public boolean shutDown = false;
    public int zoneId = 0;
    public int protocol = 0;

    public IRequestMessage createNew() {
        return new LoginRequest();
    }

}
