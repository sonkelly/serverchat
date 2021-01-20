package allinone.protocol.messages;

import java.util.ArrayList;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ItemEntity;

public class ItemListResponse extends AbstractResponseMessage {

    public String errMgs;
    public ArrayList<ItemEntity> data = new ArrayList<ItemEntity>();
    public int itemType; 
    public int cardSize;
    public int itemSize;
    
    public IResponseMessage createNew() {
        return new ItemListResponse();
    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        errMgs = aErrorMsg;
    }
    public void setSuccess(int aCode, ArrayList<ItemEntity> data) {
        mCode = aCode;
        this.data = data;
    }
}
