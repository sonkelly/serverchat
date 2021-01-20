/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import java.util.ArrayList;
import java.util.List;
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
import allinone.data.QueueImageEntity;
import allinone.data.RateEntity;
import allinone.data.ResponseCode;
import allinone.protocol.messages.GetNewestAlbumRequest;
import allinone.protocol.messages.GetNewestAlbumResponse;
import allinone.protocol.messages.GetSocialAvatarResponse;
import allinone.queue.data.ImageQueue;




/**
 *
 * @author mcb
 */
public class GetNewestAlbumBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetNewestAlbumBusiness.class);
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("get friends blog");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetNewestAlbumResponse resBlogs = (GetNewestAlbumResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
            
            GetNewestAlbumRequest rqAlbum = (GetNewestAlbumRequest) aReqMsg;
            
            CacheAlbumInfo cacheInfo = new CacheAlbumInfo();
            
          
            List<AlbumEntity> albums= cacheInfo.getNewestAlbums();
            int fromIndex = rqAlbum.page * rqAlbum.size;
            int toIndex = fromIndex + rqAlbum.size;
            int albumSize = albums.size();
            
            if(toIndex> albumSize)
            {
                toIndex = albumSize;
            }
            
            List<AlbumEntity> retAlbum = new ArrayList<AlbumEntity>();
            for(int i = fromIndex; i< toIndex; i++)
            {
                retAlbum.add(albums.get(i));
            }
            
            StringBuilder sb = new StringBuilder();
            
            int retSize = retAlbum.size();
            for(int i = 0; i< retSize; i++)
            {
                AlbumEntity entity = retAlbum.get(i);
                
                sb.append(entity.getAlbumId()).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(entity.getName()).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(entity.getImageCount()).append(AIOConstants.SEPERATOR_BYTE_1);
                RateEntity rtEntity = entity.getRateEntity();
                
                sb.append(rtEntity.getViewCount()).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(rtEntity.getLikeCount()).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(rtEntity.getCommentCount()).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(entity.getIconId()).append(AIOConstants.SEPERATOR_BYTE_2);
            }
            
            if(retSize>0)
            {
                sb.deleteCharAt(sb.length() -1);
            }
            
            sb.append(AIOConstants.SEPERATOR_BYTE_3).append(rqAlbum.page);
                    
            if(rqAlbum.page == 0)
            {
                int countPage = (int)Math.ceil((float)albumSize/(float)rqAlbum.size);
                sb.append(AIOConstants.SEPERATOR_BYTE_1).append(countPage);
            }
            
            
            
            resBlogs.value = sb.toString();
            resBlogs.mCode = ResponseCode.SUCCESS;
            UUID imgRequest = UUID.randomUUID();
            aSession.setImageRequest(imgRequest);
//            if(rqBlog.page== 1)
//                resBlogs.numBlog = numBlog[0];
            
            aSession.write(resBlogs);
            
            long currentTime = System.currentTimeMillis();
            for(int i = 0; i< retSize; i++)
            {
                AlbumEntity entity = retAlbum.get(i);
                if(entity.getIconId()>0)
                {
                    //put into queue
                    
                    GetSocialAvatarResponse queueAvar = (GetSocialAvatarResponse)msgFactory.getResponseMessage(MessagesID.GET_SOCIAL_AVATAR);
                    queueAvar.mCode = ResponseCode.SUCCESS;
    //                QueueImageEntity imgEntity = new QueueImageEntity(entity.getFileId(), false, aSession, queueAlbum, rqAlb.albumId);
                    QueueImageEntity imgEntity = new QueueImageEntity(entity.getIconId(), true,
                            aSession, queueAvar);
                    
                    imgEntity.setRequestImgId(imgRequest);
                    imgEntity.setRequestTime(currentTime);
                    
                    ImageQueue imgQueue = new ImageQueue();
                    imgQueue.insertImage(imgEntity);
                
                }
            }
        } catch (ServerException ex) {
            resBlogs.setFailure("Có lỗi xảy ra");
            mLog.error(ex.getMessage(), ex);
        } 
        finally
        {
            if (resBlogs.mCode == ResponseCode.FAILURE)
            {
                try {
                    aSession.write(resBlogs);
                } catch (ServerException ex) {
                    mLog.error(ex.getMessage(), ex);
                }
                        
            }
        }        
        return 1;
    }
}
