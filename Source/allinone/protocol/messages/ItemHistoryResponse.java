package allinone.protocol.messages;

import java.util.ArrayList;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ItemOrderEntity;

public class ItemHistoryResponse extends AbstractResponseMessage {

    public String errMgs;
    public ArrayList<ItemOrderEntity> data = new ArrayList<ItemOrderEntity>();
    public String value;
    

    public IResponseMessage createNew() {
        return new ItemHistoryResponse();
    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        errMgs = aErrorMsg;
    }

    public void setSuccess(int aCode, ArrayList<ItemOrderEntity> data) {
        mCode = aCode;
        this.data = data;
    }
}
