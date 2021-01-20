/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import java.util.List;
import java.util.UUID;

import tools.CacheFriendsInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.MessagesID;
import allinone.data.QueueImageEntity;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.protocol.messages.GetSocialAvatarResponse;
import allinone.protocol.messages.GetSocialFriendRequest;
import allinone.protocol.messages.GetSocialFriendResponse;
import allinone.queue.data.ImageQueue;




/**
 *
 * @author mcb
 */
public class GetSocialFriendBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetSocialFriendBusiness.class);
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("get social friend");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetSocialFriendResponse resalbum = (GetSocialFriendResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
            
           
            GetSocialFriendRequest rqSocial = (GetSocialFriendRequest) aReqMsg;
            
//            AlbumDB db = new AlbumDB();
            CacheFriendsInfo cache  = new CacheFriendsInfo();
                
            List<UserEntity> friends = cache.getFriends(aSession.getUID());
  
             
            
            int friendSize = friends.size();
            int fromIndex = rqSocial.pageIndex * AIOConstants.PAGE_10_DEFAULT_SIZE;
            int toIndex = fromIndex + AIOConstants.PAGE_10_DEFAULT_SIZE;
            if(toIndex>friendSize)
            {
                toIndex = friendSize;
            }
            
            StringBuilder sb = new StringBuilder();
            
            // dont paging
            fromIndex = 0;
            toIndex = friendSize;
            
            
            for(int i = fromIndex; i< toIndex; i++)
            {
                UserEntity entity = friends.get(i);
                sb.append(Long.toString(entity.mUid)).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(entity.mUsername).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Long.toString(entity.money)).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Long.toString(entity.avFileId)).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(entity.isOnline?"1":"0").append(AIOConstants.SEPERATOR_BYTE_2);
                
            }
            
            
            if(sb.length()>0)
            {
                sb.deleteCharAt(sb.length() -1 );
                sb.append(AIOConstants.SEPERATOR_BYTE_3);
                sb.append(rqSocial.pageIndex).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Math.round(friendSize/AIOConstants.PAGE_10_DEFAULT_SIZE));
            
            }
            
            UUID imgRequest = UUID.randomUUID();
            aSession.setImageRequest(imgRequest);
            
            long currentTime = System.currentTimeMillis();
            
            //send queue avatar
            for(int i = fromIndex; i< toIndex; i++)
            {
                UserEntity entity = friends.get(i);
                if(entity.avFileId>0)
                {
                    //put into queue
                    
                    
                    GetSocialAvatarResponse queueAvar = (GetSocialAvatarResponse)msgFactory.getResponseMessage(MessagesID.GET_SOCIAL_AVATAR);
                    queueAvar.mCode = ResponseCode.SUCCESS;
    //                QueueImageEntity imgEntity = new QueueImageEntity(entity.getFileId(), false, aSession, queueAlbum, rqAlb.albumId);
                    QueueImageEntity imgEntity = new QueueImageEntity(entity.avFileId, true,
                            aSession, queueAvar);
                    
                    imgEntity.setUserId(entity.mUid);
                    imgEntity.setRequestImgId(imgRequest);
                    imgEntity.setRequestTime(currentTime);
                    
                    ImageQueue imgQueue = new ImageQueue();
                    imgQueue.insertImage(imgEntity);
                
                }
            }
            
            
            
            resalbum.mCode = ResponseCode.SUCCESS; 
            resalbum.value = sb.toString();
            
        } 
//        catch(BusinessException ex)
//        {
//            mLog.warn(ex.getMessage());
//            resalbum.setFailure(ex.getMessage());
//            
//        }
        finally
        {
            if (resalbum!= null)
            {
                try {
                    aSession.write(resalbum);
                } catch (ServerException ex) {
                    mLog.error(ex.getMessage(), ex);
                }
                        
            }
        }        
        return 1;
    }
}
