/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages;

/**
 *
 * @author Zeple
 */
import allinone.data.SuperUserEntity;
import java.util.ArrayList;
import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class GetPartnerResponse
  extends AbstractResponseMessage
{
  public String mErrorMsg;
  public ArrayList<SuperUserEntity> arList;
  
  public void success(ArrayList<SuperUserEntity> ar)
  {
    this.mCode = 1;
    this.arList = ar;
  }
  
  public void setFailure(String message)
  {
    this.mCode = 0;
    this.mErrorMsg = message;
  }
  
  public IResponseMessage createNew()
  {
    return new GetPartnerResponse();
  }
}
