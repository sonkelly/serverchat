package vn.game.protocol.messages;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class ExpiredSessionResponse extends AbstractResponseMessage
{
  public String mErrorMsg;

  public IResponseMessage createNew()
  {
    return new ExpiredSessionResponse();
  }
}