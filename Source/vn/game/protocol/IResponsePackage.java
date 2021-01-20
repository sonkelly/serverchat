package vn.game.protocol;

import java.util.Vector;

import vn.game.session.ISession;

public abstract interface IResponsePackage
{
  public abstract PackageHeader getResponseHeader();

  public abstract void addMessage(IResponseMessage paramIResponseMessage);

  public abstract void addPackage(IResponsePackage paramIResponsePackage);

  public abstract void prepareEncode(ISession paramISession);

  public abstract Vector<IResponseMessage> optAllMessages();
}