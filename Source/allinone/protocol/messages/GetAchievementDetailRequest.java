package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetAchievementDetailRequest extends AbstractRequestMessage {
    public int achievementId;
    public IRequestMessage createNew()
    {
        return new GetAchievementDetailRequest();
    }
}
