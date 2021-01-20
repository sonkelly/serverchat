package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import vn.game.session.ISession;

public class MissionResponse extends AbstractResponseMessage {

    public String response;

    @Override
    public IResponseMessage createNew() {
        return new MissionResponse();
    }

    @Override
    public MissionResponse clone(ISession session) {
        MissionResponse resMsg = (MissionResponse) createNew();
        resMsg.response = response;

        return resMsg;
    }
}
