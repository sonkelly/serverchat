package vn.game.protocol;

import java.util.ArrayList;

import org.json.JSONObject;

import vn.game.bytebuffer.IByteBuffer;
import vn.game.common.ServerException;
import vn.game.session.ISession;

public abstract interface IPackageProtocol {

    public abstract IMessageProtocol getMessageProtocol(int paramInt);

    public abstract IRequestPackage decode(ISession paramISession, String paramIByteBuffer) throws ServerException;

    public abstract IRequestPackage decodeNew(ISession paramISession, IByteBuffer paramIByteBuffer) throws ServerException;

    public abstract IByteBuffer encode(ISession paramISession, IResponsePackage paramIResponsePackage) throws ServerException;
//add by zep

    public abstract String encodeWeb(ISession paramISession, IResponsePackage paramIResponsePackage) throws ServerException;

    public abstract ArrayList<JSONObject> encodeToJSON(ISession aSession, IResponsePackage aResPkg) throws ServerException;

}
//public abstract interface IPackageProtocol {
//
//    public abstract IMessageProtocol getMessageProtocol(int paramInt);
//
//    public abstract IRequestPackage decode(ISession paramISession, IByteBuffer paramIByteBuffer)
//            throws ServerException;
//
//    public abstract IRequestPackage decodeNew(ISession paramISession, IByteBuffer paramIByteBuffer)
//            throws ServerException; //Modify in order to support old version
//
//    public abstract IByteBuffer encode(ISession paramISession, IResponsePackage paramIResponsePackage)
//            throws ServerException;
//
//    public abstract ArrayList<JSONObject> encodeToJSON(ISession aSession, IResponsePackage aResPkg)
//            throws ServerException;//for new structure 
//}
