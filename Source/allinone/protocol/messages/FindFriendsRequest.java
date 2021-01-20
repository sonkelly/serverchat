package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class FindFriendsRequest extends AbstractRequestMessage
{

    public boolean isMale;
    public int countryId;
    public int cityId;
    public int jobId;
    public int fromYear;
    public int toYear;
    public int characterId;
    public boolean hasAvatar;
    public int pageIndex;
    public String name;
    
    public long requestId;
    
    public IRequestMessage createNew()
    {
        return new FindFriendsRequest();
    }

}
