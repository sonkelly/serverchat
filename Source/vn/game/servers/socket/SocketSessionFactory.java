package vn.game.servers.socket;

import vn.game.session.ISession;
import vn.game.session.ISessionFactory;

public class SocketSessionFactory
  implements ISessionFactory
{
  public ISession createSession(boolean ws)
  {
    return new SocketSession(ws);
  }

//    @Override
//    public ISession createSession(boolean ws) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
}