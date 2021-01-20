package allinone.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;

public class UpdateReferenceResponse extends AbstractResponseMessage {
  public String mErrorMsg;
  public String mess;
  public long newMoney;
  
  public UpdateReferenceResponse() {}
  
  public void setSuccess(int aCode, String mess) {
    mCode = aCode;
    this.mess = mess;
  }
  
  public void setFailure(int aCode, String aErrorMsg) {
    mCode = aCode;
    mErrorMsg = aErrorMsg;
  }
  
  @Override
  public vn.game.protocol.IResponseMessage createNew() {
    return new UpdateReferenceResponse();
  }
}
