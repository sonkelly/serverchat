/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import tools.CacheWallInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.AlertEntity;
import allinone.data.MessagesID;
import allinone.data.QueueImageEntity;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.protocol.messages.GetBoardRequest;
import allinone.protocol.messages.GetBoardResponse;
import allinone.protocol.messages.GetSocialAvatarResponse;
import allinone.protocol.messages.SendFileIconResponse;
import allinone.queue.data.ImageQueue;



/**
 *
 * @author mcb
 */
public class GetBoardBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetBoardBusiness.class);
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("get wall");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetBoardResponse resBlogs = (GetBoardResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
            
            GetBoardRequest rqBlog = (GetBoardRequest) aReqMsg;
            CacheWallInfo cache = new CacheWallInfo();
            
            List<AlertEntity> alertFulls = cache.getWall(aSession.getUID());
            StringBuilder sb = new StringBuilder();
            
            List<UserEntity> lstUsers = new ArrayList<UserEntity>();
            List<Long> lstFileIds = new ArrayList<Long>();
            List<AlertEntity> alerts = new ArrayList<AlertEntity>();
            
            int fullSize = alertFulls.size();
            for(int i = 0; i< fullSize; i++)
            {
                AlertEntity entity = alertFulls.get(i);
                if(entity.getAlertTypeId() == AIOConstants.POST_WALL_SYSTEM_OBJECT_ID)
                {
                    continue;
                }
                alerts.add(entity);
            }
            
            UUID imgRequest = UUID.randomUUID();
            aSession.setImageRequest(imgRequest);
//            UserEntity currentEntity = new UserEntity();
//            currentEntity.mUid = aSession.getUID();
//            currentEntity.mUsername = aSession.getUserName();
//            
//            lstUsers.add(currentEntity);
            
             int alertSize = alerts.size();
            
            int pageSize = rqBlog.pageSize;
            
          
            
            int fromIndex = rqBlog.pageIndex * pageSize;
            int toIndex = fromIndex + pageSize;
            
            if(toIndex> alertSize)
            {
                toIndex = alertSize;
            }
            
            
//            alertSize = alertSize> AIOConstants.MAX_ALERT? AIOConstants.MAX_ALERT: alertSize;
            for(int i = fromIndex; i< toIndex; i++)
            {
                AlertEntity entity = alerts.get(i);
                int userSize = lstUsers.size();
                int index = userSize;
                for(int j = 0; j< userSize; j++)
                {
                    if(lstUsers.get(j).mUid == entity.getUserEntity().mUid)
                    {
                        index = j; 
                        break;
                    }
                }
                
                if(index == userSize || userSize ==0)
                {
//                    CacheUserInfo cacheUser = new CacheUserInfo();
//                    UserEntity usrEntity = cacheUser.getUserInfo(entity.getUserId());
                    
//                    usrEntity.mUid = entity.getUserId();
//                    usrEntity.mUsername = entity.getUserName();
                    lstUsers.add(entity.getUserEntity());
                }
                
                sb.append(Integer.toString(index)).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Long.toString(entity.getAlertId())).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Long.toString(entity.getCreatedDate().getTime())).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(entity.getMessage()).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Integer.toString(entity.getSystemObjectId())).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Long.toString(entity.getSystemObjectRecordId())).append(AIOConstants.SEPERATOR_BYTE_2);
                if(entity.getSystemObjectId() == AIOConstants.FILE_SYSTEM_OBJECT_ID && entity.getSystemObjectRecordId()>0)
                {
                    lstFileIds.add(entity.getSystemObjectRecordId());
                }
                
            }
            
            List<UserEntity> lstAvatarFileIds = new ArrayList<UserEntity>();
            
            if(sb.length()>0)
            {
                sb.deleteCharAt(sb.length() -1);
                //add user name
                sb.append(AIOConstants.SEPERATOR_BYTE_3);
                
                int userSize = lstUsers.size();
                for(int i = 0; i< userSize; i++)
                {
                    UserEntity usrEntity = lstUsers.get(i);
                    sb.append(Long.toString(usrEntity.mUid)).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(usrEntity.mUsername).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(usrEntity.avFileId).append(AIOConstants.SEPERATOR_BYTE_2);
                    
                    if(usrEntity.avFileId>0)
                    {
                        lstAvatarFileIds.add(usrEntity);
                        
//                        //put into queue
//                        GetSocialAvatarResponse queueAvar = (GetSocialAvatarResponse)msgFactory.getResponseMessage(MessagesID.GET_SOCIAL_AVATAR);
//                        queueAvar.mCode = ResponseCode.SUCCESS;
//
//                        QueueImageEntity imgEntity = new QueueImageEntity(usrEntity.avFileId, true,
//                                aSession, queueAvar);
//
//                        imgEntity.setUserId(usrEntity.mUid);
//                        ImageQueue imgQueue = new ImageQueue();
//                        imgQueue.insertImage(imgEntity);

                    }
                }
                
                if(aSession.getByteProtocol()> AIOConstants.PROTOCOL_MXH && rqBlog.pageIndex == 0)
                {
                    sb.deleteCharAt(sb.length() -1);
                    //add user name
                    sb.append(AIOConstants.SEPERATOR_BYTE_3);
                    sb.append(rqBlog.pageIndex);
                    if(rqBlog.pageIndex == 0)
                    {
                        int countPage = (int)Math.ceil((float)alertSize/(float)pageSize);
                        sb.append(AIOConstants.SEPERATOR_BYTE_1).append(countPage);
                    }
                }
                
            }
            
            resBlogs.mCode = ResponseCode.SUCCESS;
            
            
            resBlogs.value = sb.toString();
            
            aSession.write(resBlogs);
            
            //send queue message
            long currentTime = System.currentTimeMillis();
            
            int avatarSize = lstAvatarFileIds.size();
            for(int i = 0; i< avatarSize; i++)
            {
                    //put into queue
                UserEntity usrEntity = lstAvatarFileIds.get(i);
                
                GetSocialAvatarResponse queueAvar = (GetSocialAvatarResponse)msgFactory.getResponseMessage(MessagesID.GET_SOCIAL_AVATAR);
                queueAvar.mCode = ResponseCode.SUCCESS;

                QueueImageEntity imgEntity = new QueueImageEntity(usrEntity.avFileId, true,
                        aSession, queueAvar);
                imgEntity.setRequestImgId(imgRequest);
                imgEntity.setRequestTime(currentTime);
                
                imgEntity.setUserId(usrEntity.mUid);
                ImageQueue imgQueue = new ImageQueue();
                imgQueue.insertImage(imgEntity);
            }
            
            int thumbSize = lstFileIds.size();
            for(int i = 0; i< thumbSize; i++)
            {
                
                SendFileIconResponse queueAlbum = (SendFileIconResponse)msgFactory.getResponseMessage(MessagesID.SEND_IMAGE_ICON);
                queueAlbum.mCode = ResponseCode.SUCCESS;
//                QueueImageEntity imgEntity = new QueueImageEntity(entity.getFileId(), false, aSession, queueAlbum, rqAlb.albumId);
                QueueImageEntity imgEntity = new QueueImageEntity(lstFileIds.get(i), true, aSession, queueAlbum);
                if(aSession.getDeviceType() == AIOConstants.ANDROID_DEVICE)
                {
                    imgEntity.setIsIcon(false);
                }
                
                imgEntity.setRequestImgId(imgRequest);
                imgEntity.setRequestTime(currentTime);
//                imgEntity.setFileEntity(entity);
                ImageQueue imgQueue = new ImageQueue();
                imgQueue.insertImage(imgEntity);
                
            }
            
            
            
        } 
        catch (ServerException ex) {
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
