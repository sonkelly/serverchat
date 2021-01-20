/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;



import org.slf4j.Logger;

import tools.CacheAlbumInfo;
import tools.CacheBlogInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.ResponseCode;
import allinone.databaseDriven.WallDB;
import allinone.protocol.messages.DeleteItemRequest;
import allinone.protocol.messages.DeleteItemResponse;

/**
 *
 * @author mcb
 */
public class DeleteItemBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(DeleteItemBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
//        mLog.debug("[BET] : Catch  ; " + aSession.getUserName());
        MessageFactory msgFactory =  aSession.getMessageFactory();
        DeleteItemResponse resBet = (DeleteItemResponse) msgFactory
                        .getResponseMessage(aReqMsg.getID());
        
        try {
                DeleteItemRequest rqAddComment = (DeleteItemRequest) aReqMsg;
                WallDB db = new WallDB();
                long ret = db.deleteItem(rqAddComment.systemObjectId, 
                        rqAddComment.systemObjectRecordId, aSession.getUID());
                if(ret< 0)
                {
                    throw new BusinessException("Bạn không có quyền xóa đối tượng này");
                }
                resBet.mCode = ResponseCode.SUCCESS;
                aSession.write(resBet);
                
                if(rqAddComment.systemObjectId == AIOConstants.BLOG_SYSTEM_OBJECT_ID)
                {
                    CacheBlogInfo cacheInfo = new CacheBlogInfo();
                    cacheInfo.deleteCache(aSession.getUID());
                }
                else if(rqAddComment.systemObjectId == AIOConstants.FILE_SYSTEM_OBJECT_ID)
                {
                    CacheAlbumInfo cacheInfo = new CacheAlbumInfo();
                    cacheInfo.deleteCacheAlbum(ret);
                }
                else if(rqAddComment.systemObjectId == AIOConstants.ALBUM_SYSTEM_OBJECT_ID)
                {
                    CacheAlbumInfo cacheInfo = new CacheAlbumInfo();
                    cacheInfo.deleteCacheAlbums(aSession.getUID());
                }
        }
        catch(BusinessException be)
        {
            resBet.setFailure(ResponseCode.FAILURE, be.getMessage());
            aResPkg.addMessage(resBet);
        }
                   
        
        catch (Throwable t) {
                //resBet.setFailure(ResponseCode.FAILURE, t.getMessage());
                mLog.error("Process message " + aReqMsg.getID() + " error.", t);
                try {
                        aSession.write(resBet);
                } catch (ServerException ex) {
                        // java.util.logging.Logger.getLogger(TurnBusiness.class.getName()).log(Level.SEVERE,
                        // null, ex);
                }

        } 
        
        return 1;
    }
}
