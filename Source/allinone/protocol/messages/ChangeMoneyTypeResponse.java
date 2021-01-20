package allinone.protocol.messages;



import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import vn.game.session.ISession;

public class ChangeMoneyTypeResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public int zoneID;
    public int roomID;
    public long matchID;
    
   
    public int moneyType;
   
    
    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }
    
    public void setSuccess(int aCode,int _moneyType) {
        mCode = aCode;
        moneyType = _moneyType;
    }
    
    @Override
    public IResponseMessage createNew() {
        return new ChangeMoneyTypeResponse();
    }
    
    @Override
     public IResponseMessage clone(ISession session)
     {
          ChangeMoneyTypeResponse resMsg = (ChangeMoneyTypeResponse)createNew();

            resMsg.session = session;
            resMsg.setID(this.getID());
            resMsg.mCode = mCode;
            resMsg.mErrorMsg = mErrorMsg;
            resMsg.moneyType = moneyType;
           
          return resMsg;  
     }
    
                
}
