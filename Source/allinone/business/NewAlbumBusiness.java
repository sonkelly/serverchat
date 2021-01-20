/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;



import org.slf4j.Logger;

import tools.CacheAlbumInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.databaseDriven.AlbumDB;
import allinone.protocol.messages.NewAlbumRequest;
import allinone.protocol.messages.NewAlbumResponse;

/**
 *
 * @author mcb
 */
public class NewAlbumBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(NewAlbumBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
//        mLog.debug("[BET] : Catch  ; " + aSession.getUserName());
        MessageFactory msgFactory =  aSession.getMessageFactory();
        NewAlbumResponse resBet = (NewAlbumResponse) msgFactory
                        .getResponseMessage(aReqMsg.getID());
        
        try {
                NewAlbumRequest rqNewBlog = (NewAlbumRequest) aReqMsg;
                AlbumDB db = new AlbumDB();
                resBet.albumId =  db.insertAlbum(aSession.getUID(), rqNewBlog.name);
                resBet.mCode = ResponseCode.SUCCESS;
                aSession.write(resBet);
                CacheAlbumInfo cacheAlbum = new CacheAlbumInfo();
                cacheAlbum.deleteCacheAlbums(aSession.getUID());
        }
        
        catch (Throwable t) {
                //resBet.setFailure(ResponseCode.FAILURE, t.getMessage());
                mLog.error("Process message " + aReqMsg.getID() + " error.", t);
                try {
                    resBet.mErrorMsg = "Có lỗi xảy ra";
                    aSession.write(resBet);
                } catch (ServerException ex) {
                        // java.util.logging.Logger.getLogger(TurnBusiness.class.getName()).log(Level.SEVERE,
                        // null, ex);
                }

        } 
        
        return 1;
    }
}
