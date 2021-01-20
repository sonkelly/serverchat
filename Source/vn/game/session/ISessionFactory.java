package vn.game.session;

public abstract interface ISessionFactory
{
  public abstract ISession createSession(boolean ws);
}