package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class UploadAvatarRequest extends AbstractRequestMessage{
    public long albumId;
    public boolean isCancel;
    public int imageId;
    public int sequence;
    public String detail;
    public int maxParts;
    @Override
    public IRequestMessage createNew()
    {
        return new UploadAvatarRequest();
    }
}
