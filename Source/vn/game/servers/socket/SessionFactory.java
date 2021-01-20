package vn.game.servers.socket;

import vn.game.session.ISession;
import vn.game.session.ISessionFactory;

public class SessionFactory implements ISessionFactory {
	@Override
	public ISession createSession(boolean ws) {
		return new Session(ws);
	}
}