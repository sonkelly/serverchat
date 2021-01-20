/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import java.util.List;
import java.util.UUID;

import tools.CacheUserInfo;
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
import allinone.protocol.messages.GetTopBloggerResponse;
import allinone.queue.data.ImageQueue;




/**
 *
 * @author mcb
 */
public class GetTopBloggerBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetTopBloggerBusiness.class);
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("get friends blog");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetTopBloggerResponse resBlogs = (GetTopBloggerResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
            
            
            
            CacheUserInfo cacheInfo = new CacheUserInfo();
            
          
            List<UserEntity> users= cacheInfo.getTopBlogger();
            
            int userSize = users.size();
            StringBuilder sb= new StringBuilder();
            for(int i = 0; i< userSize; i++)
            {
                UserEntity usrEntity = users.get(i);
                    
                sb.append(usrEntity.mUid).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(usrEntity.mUsername).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(usrEntity.avFileId).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(usrEntity.stt).append(AIOConstants.SEPERATOR_BYTE_2);
                
                
                
            }
            
            if(userSize>0)
            {
                sb.deleteCharAt(sb.length() -1);
            }
            
            resBlogs.value = sb.toString();
            resBlogs.mCode = ResponseCode.SUCCESS;
//            if(rqBlog.page== 1)
//                resBlogs.numBlog = numBlog[0];
            
            aSession.write(resBlogs);
            
            UUID imgRequest = UUID.randomUUID();
            aSession.setImageRequest(imgRequest);
            
            long currentTime = System.currentTimeMillis();
            
            for(int i = 0; i< userSize; i++)
            {
                UserEntity entity = users.get(i);
                if(entity.avFileId>0)
                {
                    //put into queue
                    GetSocialAvatarResponse queueAvar = (GetSocialAvatarResponse)msgFactory.getResponseMessage(MessagesID.GET_SOCIAL_AVATAR);
                    queueAvar.mCode = ResponseCode.SUCCESS;
    
                    QueueImageEntity imgEntity = new QueueImageEntity(entity.avFileId, true,
                            aSession, queueAvar);
                    
                    imgEntity.setRequestImgId(imgRequest);
                    imgEntity.setRequestTime(currentTime);
                    imgEntity.setUserId(entity.mUid);
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
