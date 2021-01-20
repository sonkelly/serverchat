package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class PlaySocialLoginRequest extends AbstractRequestMessage {

    public int partnerId = 0;
    public String refCode = "";
    public String socialId = "";
    public String mobileVersion = "";
    public int deviceType = 4;
    public String fullName = "";
    public String device = "";
    public String token = "";
    public String deviceID = "";

    @Override
    public IRequestMessage createNew() {
        return new PlaySocialLoginRequest();
    }

}
