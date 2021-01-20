package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;

public class CanReferenceResponse extends AbstractResponseMessage {
  public String message;
  
  public CanReferenceResponse() {}
  
  public boolean canReference = false;
  public long activateTime = 0L;
  public String active;
  public boolean ispartner = false;
  public boolean haveMob = true;
  public String sms_active = "";
  
  public void setSuccess(boolean _ispartner) {
    mCode = 1;
    canReference = true;
    ispartner = _ispartner;
  }
  
  public void setFailure(String msg) {
    mCode = 0;
    message = msg;
  }
  
  public vn.game.protocol.IResponseMessage createNew()
  {
    return new CanReferenceResponse();
  }
}
