package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class IAPRequest extends AbstractRequestMessage {
    
    public String receiptData;
    public String receiptPassword;
    public String type = "1";//1 gooogle, 2 apple    
    public IRequestMessage createNew() {
        return new IAPRequest();
    }
}
