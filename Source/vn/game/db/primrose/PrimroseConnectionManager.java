package vn.game.db.primrose;

import vn.game.db.AbstractConnectionManager;
import vn.game.db.DBException;
import vn.game.db.IConnectionMaker;

public class PrimroseConnectionManager extends AbstractConnectionManager
{
  private IConnectionMaker mMaker = null;

  protected PrimroseConnectionManager()
  {
    this.mMaker = new PrimroseConnectionMaker();
  }

  public IConnectionMaker getConnectionMaker()
    throws DBException
  {
    if (this.mMaker == null)
    {
      this.mMaker = new PrimroseConnectionMaker();
    }

    return this.mMaker;
  }

  public void destroy()
  {
    if (this.mMaker != null)
    {
      this.mMaker.destroy();
    }
  }

  public void init(String aSource)
    throws DBException
  {
    ((PrimroseConnectionMaker)this.mMaker).init(aSource);
  }
}