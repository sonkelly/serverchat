package allinone.protocol.messages;


import java.util.List;
import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;




public class CancelShowHandRequest
  extends AbstractRequestMessage
{
  //public List<BauCuaTomCaPlayer> players;
  public String lstPlayerId;
  
  public CancelShowHandRequest() {}
  
  public IRequestMessage createNew()
  {
    return new CancelShowHandRequest();
  }
}
