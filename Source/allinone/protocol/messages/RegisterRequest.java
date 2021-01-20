package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class RegisterRequest extends AbstractRequestMessage {

    public String mUsername;
    public String mPassword;
    public int mAge;
    public boolean isMale;
    public String mail;
    public String phone;
    public String cp = "0";
    public String key;
    public int partnerId;
    public int protocol;
    public String linkGuiTang;
    public long refGioiThieuId;
    public long gioiThieuUid;
    public boolean isMxh;
    public String refCode = "0";
    public int deviceType; //0: j2me 1:android 2:iphone 3 window phones
    public String device = "";//android,ios, windowphone,web, other
    public String clientSessionId;
    public String deviceUID;

    public int registerTime = 0;

    public IRequestMessage createNew() {
        return new RegisterRequest();
    }
}
