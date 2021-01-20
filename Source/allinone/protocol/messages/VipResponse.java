package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import vn.game.session.ISession;

public class VipResponse extends AbstractResponseMessage {

    public String response;

    @Override
    public IResponseMessage createNew() {
        return new VipResponse();
    }

    @Override
    public VipResponse clone(ISession session) {
        VipResponse resMsg = (VipResponse) createNew();
        resMsg.response = response;

        return resMsg;
    }
}
