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
import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

public class GetPartnerRequest
  extends AbstractRequestMessage
{
  public IRequestMessage createNew()
  {
    return new GetPartnerRequest();
  }
}
