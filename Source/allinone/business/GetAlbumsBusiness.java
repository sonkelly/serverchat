/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import java.util.UUID;

import tools.CacheAlbumInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.AlbumEntity;
import allinone.data.MessagesID;
import allinone.data.PositionEntity;
import allinone.data.QueueImageEntity;
import allinone.data.ResponseCode;
import allinone.protocol.messages.GetAlbumsRequest;
import allinone.protocol.messages.GetAlbumsResponse;
import allinone.protocol.messages.SendAlbumIconResponse;
import allinone.queue.data.ImageQueue;




/**
 *
 * @author mcb
 */
public class GetAlbumsBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetAlbumsBusiness.class);
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("get albums");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetAlbumsResponse resalbums = (GetAlbumsResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
            
            GetAlbumsRequest rqBlog = (GetAlbumsRequest) aReqMsg;
            
//            AlbumDB db = new AlbumDB();
            CacheAlbumInfo cache  = new CacheAlbumInfo();
            resalbums.albums = cache.getAlbums(rqBlog.mUid);
            
            resalbums.mCode = ResponseCode.SUCCESS;
             
            aSession.write(resalbums);
           
            PositionEntity sessPos = new PositionEntity(AIOConstants.ALBUMS_POSITION, rqBlog.mUid);
            aSession.setCurrPosition(sessPos);
            UUID imgRequest = UUID.randomUUID();
            aSession.setImageRequest(imgRequest);
            long currentTime = System.currentTimeMillis();
            
            
            //put send icon album into queue
            int albumSize = resalbums.albums.size();
            for(int i = 0; i< albumSize; i++)
            {
                AlbumEntity entity = resalbums.albums.get(i);
                if(entity.getIconId()>0)
                {
                    SendAlbumIconResponse resAlbumIcon = (SendAlbumIconResponse) msgFactory.getResponseMessage(MessagesID.SEND_ALBUM_ICON);
                    resAlbumIcon.setSuccess(ResponseCode.SUCCESS);
                    
                    //this album has files
                    QueueImageEntity imgEntity = new QueueImageEntity(entity.getIconId(), true, aSession, resAlbumIcon);
                    
                    imgEntity.setRequestImgId(imgRequest);
                    imgEntity.setRequestTime(currentTime);
                    ImageQueue imgQueue = new ImageQueue();
                    imgQueue.insertImage(imgEntity);
                    
                }
            }
            
            
        } 
        catch (ServerException ex) {
            mLog.error(ex.getMessage(), ex);
            resalbums.setFailure("co loi xay ra");
            aResPkg.addMessage(resalbums);
        }        
        finally
        {
//            if (resalbums!= null)
//            {
//                try {
//                    aSession.write(resalbums);
//                } catch (ServerException ex) {
//                    mLog.error(ex.getMessage(), ex);
//                }
//                        
//            }
        }        
        return 1;
    }
}
