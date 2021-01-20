/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import tools.CacheCommentInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.CommentEntity;
import allinone.data.MessagesID;
import allinone.data.QueueImageEntity;
import allinone.data.ResponseCode;
import allinone.protocol.messages.GetCommentRequest;
import allinone.protocol.messages.GetCommentResponse;
import allinone.protocol.messages.GetSocialAvatarResponse;
import allinone.queue.data.ImageQueue;




/**
 *
 * @author mcb
 */
public class GetCommentBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetCommentBusiness.class);
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("get albums");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetCommentResponse resComment = (GetCommentResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
            
            GetCommentRequest rqComment = (GetCommentRequest) aReqMsg;
            

            CacheCommentInfo cache  = new CacheCommentInfo();
            

            
            List<CommentEntity> lstComments = cache.getComment(rqComment.systemObjectId, rqComment.systemObjectRecordId);
            
            
            
            int fromIndex = (rqComment.page) * rqComment.size;
            int toIndex = fromIndex+ rqComment.size;
            int fileSize = lstComments.size();
            toIndex = toIndex< fileSize? toIndex: fileSize;
            
            List<CommentEntity> diffUsers = new ArrayList<CommentEntity>();
            
            StringBuilder sb = new StringBuilder();
            
            for(int i = fromIndex; i< toIndex; i++)
            {
                CommentEntity entity = lstComments.get(i);
                int userSize = diffUsers.size();
                int index = userSize;
                for(int j = 0; j< userSize; j++)
                {
                    if(diffUsers.get(j).getUid() == entity.getUid())
                    {
                        index = j; 
                        break;
                    }
                }
                
                if(index == userSize || userSize == 0)
                {
                    diffUsers.add(entity);
                }
                
                sb.append(Integer.toString(index)).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Long.toString(entity.getCommentId())).append(AIOConstants.SEPERATOR_BYTE_1);
                
                sb.append(entity.getContent()).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Long.toString(entity.getUpdatedDate().getTime())).append(AIOConstants.SEPERATOR_BYTE_2);
                
            }
            
            int userSize = diffUsers.size();
            
            if(sb.length()>0)
            {
                sb.replace(sb.length() -1, sb.length(), AIOConstants.SEPERATOR_BYTE_3);
                
                for(int i = 0; i< userSize; i++)
                {
                    CommentEntity entity = diffUsers.get(i);
                    sb.append(Long.toString(entity.getUid())).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(entity.getUserName()).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(Long.toString(entity.getAvatarFileId())).append(AIOConstants.SEPERATOR_BYTE_2);
                }
                
                sb.deleteCharAt(sb.length() -1);
                
                if(aSession.getByteProtocol()> AIOConstants.PROTOCOL_MXH)
                {
                    sb.append(AIOConstants.SEPERATOR_BYTE_3);
                    sb.append(rqComment.page);
                    if(rqComment.page == 0)
                    {

                        int countPage = (int)Math.ceil((float)fileSize/(float)rqComment.size);
                        sb.append(AIOConstants.SEPERATOR_BYTE_1).append(countPage);
                    }

                }
                
            }
            
            resComment.mCode = ResponseCode.SUCCESS;        
            
            
            resComment.value = sb.toString();
            aSession.write(resComment);
            
            UUID imgRequest = UUID.randomUUID();
            aSession.setImageRequest(imgRequest);
            long currentTime = System.currentTimeMillis();
            
            //send icon to client    
//            CacheFileInfo cacheFile  = new CacheFileInfo();
             userSize = diffUsers.size();
            for(int i = 0; i< userSize; i++)
            {
                CommentEntity entity = diffUsers.get(i);
                if(entity.getAvatarFileId()>0)
                {
                    //put into queue
                    
                    
                    GetSocialAvatarResponse queueAvar = (GetSocialAvatarResponse)msgFactory.getResponseMessage(MessagesID.GET_SOCIAL_AVATAR);
                    queueAvar.mCode = ResponseCode.SUCCESS;
    //                QueueImageEntity imgEntity = new QueueImageEntity(entity.getFileId(), false, aSession, queueAlbum, rqAlb.albumId);
                    QueueImageEntity imgEntity = new QueueImageEntity(entity.getAvatarFileId(), true,
                            aSession, queueAvar);
                    
                    imgEntity.setUserId(entity.getUid());
                    imgEntity.setRequestImgId(imgRequest);
                    imgEntity.setRequestTime(currentTime);
                    
                    ImageQueue imgQueue = new ImageQueue();
                    imgQueue.insertImage(imgEntity);
                
                }
            }
            
            
        } 
         catch (ServerException ex) {
                mLog.error(ex.getMessage(), ex);
            }
//        catch(BusinessException ex)
//        {
//            mLog.warn(ex.getMessage());
//            resalbum.setFailure(ex.getMessage());
//            
//        }
        finally
        {
            
        }        
        return 1;
    }
}
