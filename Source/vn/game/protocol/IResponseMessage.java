package vn.game.protocol;

import vn.game.session.ISession;

public abstract interface IResponseMessage
{
    public abstract void setDestUID(long destUID);
    public abstract long getDestID();
    
    
    public abstract void setSession(ISession session);
    public abstract int getID();

    public abstract IResponseMessage createNew();
    public abstract IResponseMessage clone(ISession session);
}