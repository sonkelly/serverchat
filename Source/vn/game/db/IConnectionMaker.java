package vn.game.db;

public abstract interface IConnectionMaker
{
  public abstract void destroy();

  public abstract IConnection makeConnection()
    throws DBException;
}