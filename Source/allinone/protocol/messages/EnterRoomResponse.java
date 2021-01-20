package allinone.protocol.messages;

import java.util.List;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.SimpleTable;

public class EnterRoomResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public int zoneID;
    public List<SimpleTable> tables = null;
    
    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }
    public void setZoneID (int id){
    	this.zoneID = id;
    }
    
   
  
    
    public void setSuccess(int aCode, List<SimpleTable> t) {
        mCode = aCode;
        this.tables = t;
    }
    
    public IResponseMessage createNew() {
        return new EnterRoomResponse();
    }
}
