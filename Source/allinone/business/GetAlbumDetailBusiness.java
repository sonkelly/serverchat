/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

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
import allinone.data.FileEntity;
import allinone.data.MessagesID;
import allinone.data.PositionEntity;
import allinone.data.QueueImageEntity;
import allinone.data.ResponseCode;
import allinone.protocol.messages.GetAlbumDetailRequest;
import allinone.protocol.messages.GetAlbumDetailResponse;
import allinone.protocol.messages.SendFileIconResponse;
import allinone.queue.data.ImageQueue;




/**
 *
 * @author mcb
 */
public class GetAlbumDetailBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetAlbumDetailBusiness.class);
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("get albums");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetAlbumDetailResponse resalbum = (GetAlbumDetailResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
            
            GetAlbumDetailRequest rqAlb = (GetAlbumDetailRequest) aReqMsg;
            
//            AlbumDB db = new AlbumDB();
            CacheAlbumInfo cache  = new CacheAlbumInfo();
                  
            List<FileEntity> filesEntity = cache.getAlbumDetail(rqAlb.albumId);
            
            
            
            int fromIndex = (rqAlb.page) * rqAlb.size;
            int toIndex = fromIndex+ rqAlb.size;
            int fileSize = filesEntity.size();
            toIndex = toIndex< fileSize? toIndex: fileSize;
            resalbum.mCode = ResponseCode.SUCCESS;        
            
            if(rqAlb.page == 0)
            {
                //send list image id to client
                StringBuilder sb = new StringBuilder();
                for(int i = 0; i< fileSize; i++)
                {
                    FileEntity entity = filesEntity.get(i);
                    sb.append(entity.getFileId()).append(AIOConstants.SEPERATOR_BYTE_2);
                }
                
                if(sb.length()>0)
                {
                    sb.deleteCharAt(sb.length() -1);
                }
                resalbum.value = sb.toString();
            }
            
            aSession.write(resalbum);
            
            UUID imgRequest = UUID.randomUUID();
            aSession.setImageRequest(imgRequest);
            long currentTime = System.currentTimeMillis();
            
            PositionEntity sessPos = new PositionEntity(AIOConstants.ALBUM_DETAIL_POSITION, rqAlb.albumId);
            aSession.setCurrPosition(sessPos);
            
            
            for(int i = fromIndex; i< toIndex; i++)
            {
                FileEntity entity = filesEntity.get(i);
                entity.setFileIndex(i);
                SendFileIconResponse queueAlbum = (SendFileIconResponse)msgFactory.getResponseMessage(MessagesID.SEND_IMAGE_ICON);
                queueAlbum.mCode = ResponseCode.SUCCESS;
                queueAlbum.isSendAlbumDetail = true;
//                QueueImageEntity imgEntity = new QueueImageEntity(entity.getFileId(), false, aSession, queueAlbum, rqAlb.albumId);
                QueueImageEntity imgEntity = new QueueImageEntity(entity.getFileId(), true, aSession, queueAlbum);
                imgEntity.setRequestImgId(imgRequest);
                imgEntity.setRequestTime(currentTime);
                
                imgEntity.setFileEntity(entity);
                ImageQueue imgQueue = new ImageQueue();
                imgQueue.insertImage(imgEntity);
                
            }
            
            
            
            

            
            
        } 
        catch (ServerException ex) {
            mLog.error(ex.getMessage(), ex);
            resalbum.setFailure("co loi xay ra");
            aResPkg.addMessage(resalbum);
        }//        catch(BusinessException ex)
//        {
//            mLog.warn(ex.getMessage());
//            resalbum.setFailure(ex.getMessage());
//            
//        }
        finally
        {
//            if (resalbum!= null)
//            {
//                try {
//                    aSession.write(resalbum);
//                } catch (ServerException ex) {
//                    mLog.error(ex.getMessage(), ex);
//                }
//                        
//            }
        }        
        return 1;
    }
}
