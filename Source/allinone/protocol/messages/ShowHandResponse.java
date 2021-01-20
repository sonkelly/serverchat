package allinone.protocol.messages;




import org.json.JSONObject;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ResponseCode;

public class ShowHandResponse extends AbstractResponseMessage {

    public JSONObject showHandJson;
    
    public void setSuccess(JSONObject showHandJson)
    {
        this.showHandJson = showHandJson;
        mCode = ResponseCode.SUCCESS;
    }
    
    public IResponseMessage createNew() {
        return new ShowHandResponse();
    }
}
