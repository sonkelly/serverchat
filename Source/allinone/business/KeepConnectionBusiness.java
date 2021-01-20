package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.protocol.messages.KeepConnectionResponse;
import vn.game.common.SessionUtils;

public class KeepConnectionBusiness
  extends AbstractBusiness
{
  private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(KeepConnectionBusiness.class);
  
  public KeepConnectionBusiness() {}
  
  public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg)
    throws ServerException
  {
    MessageFactory msgFactory = aSession.getMessageFactory();
    KeepConnectionResponse resReconn = (KeepConnectionResponse)msgFactory
      .getResponseMessage(aReqMsg.getID());
    try
    {
     // session = aSession;
      //mLog.debug("vao KeepConnectionBusiness");
      //aSession.write(resReconn);
      aSession.ping(aSession);
    } catch (Throwable e) {
      mLog.error(e.getMessage());
    }
    return 1;
  }
}

