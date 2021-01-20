/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import tools.CacheBlogInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.BlogEntity;
import allinone.data.MessagesID;
import allinone.data.QueueImageEntity;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.protocol.messages.GetSocialAvatarResponse;
import allinone.protocol.messages.GetTopBlogRequest;
import allinone.protocol.messages.GetTopBlogResponse;
import allinone.queue.data.ImageQueue;




/**
 *
 * @author mcb
 */
public class GetTopBlogBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetTopBlogBusiness.class);
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("get friends blog");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetTopBlogResponse resBlogs = (GetTopBlogResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
            
            GetTopBlogRequest rqBlog = (GetTopBlogRequest) aReqMsg;
            
            CacheBlogInfo cacheInfo = new CacheBlogInfo();
            
          
            List<BlogEntity> blogs= cacheInfo.getTopBlog();
            int fromIndex = rqBlog.page * rqBlog.size;
            int toIndex = fromIndex + rqBlog.size;
            int blogSize = blogs.size();
            if(toIndex> blogSize)
            {
                toIndex = blogSize;
            }
            
            List<BlogEntity> retBlog = new ArrayList<BlogEntity>();
            for(int i = fromIndex; i< toIndex; i++)
            {
                retBlog.add(blogs.get(i));
            }
            
            StringBuilder sb = new StringBuilder();
            List<UserEntity> diffUsers = new ArrayList<UserEntity>();
            int retSize = retBlog.size();
            for(int i = 0; i< retSize; i++)
            {
                BlogEntity entity = retBlog.get(i);
                
                int userSize = diffUsers.size();
                int index = userSize;
                for(int j = 0; j< userSize; j++)
                {
                    if(diffUsers.get(j).mUid== entity.getUserId())
                    {
                        index = j; 
                        break;
                    }
                }
                
                if(index == userSize || userSize == 0)
                {
                    UserEntity usrEntity = new UserEntity();
                    usrEntity.mUid = entity.getUserId();
                    usrEntity.mUsername = entity.getUserName();
                    usrEntity.avFileId = entity.getAvFileId();
                    diffUsers.add(usrEntity);
                }
                
                sb.append(index).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(entity.getBlogId()).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(entity.getTitle()).append(AIOConstants.SEPERATOR_BYTE_1);
                if(aSession.getDeviceType() == AIOConstants.ANDROID_DEVICE && 
                        entity.getSubject() != null && entity.getSubject().length() == AIOConstants.MAX_BLOG_MAIN_IDEA)
                {
                    //change mainIdea
                    String post = entity.getPost();
                    int subjectLen = post.length();
                    if(subjectLen> AIOConstants.MAX_BLOG_MAIN_IDEA_ANDROID)
                    {
                        subjectLen = AIOConstants.MAX_BLOG_MAIN_IDEA_ANDROID;
                    }
                    String mainIdea = post.substring(0, subjectLen);
                    entity.setSubject(mainIdea);
                    entity.setMore(post.length()>mainIdea.length());
                }
                
                sb.append(entity.getSubject()).append(AIOConstants.SEPERATOR_BYTE_1);
                Date dt = entity.getCreatedDate();
                boolean isUpdatedDate = false;
                if(entity.getUpdatedDate() != null)
                {
                    dt = entity.getUpdatedDate();
                    isUpdatedDate = true;
                }
                
                
                
                sb.append(dt.getTime()).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(isUpdatedDate?"1":"0").append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(entity.getViewCount()).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(entity.getLikeCount()).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(entity.getCommentCount()).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(entity.isMore()?"1":"0").append(AIOConstants.SEPERATOR_BYTE_2);
            }
            int userSize = diffUsers.size();
            if(aSession.getByteProtocol()> AIOConstants.PROTOCOL_MXH)
            {
                if(retSize>0)
                {
                    sb.deleteCharAt(sb.length() -1);
//                    sb.append(AIOConstants.SEPERATOR_BYTE_3);
    //                sb.append(Integer.toString(blogs.size())).append(AIOConstants.SEPERATOR_BYTE_1);
    //                sb.append(Integer.toString(rqBlog.page));

                    //add usersize
                    sb.append(AIOConstants.SEPERATOR_BYTE_3);

                    for(int i = 0; i<userSize; i++ )
                    {
                        UserEntity usrEntity = diffUsers.get(i);
                        sb.append(usrEntity.mUid).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(usrEntity.mUsername).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(usrEntity.avFileId).append(AIOConstants.SEPERATOR_BYTE_2);
                    }

                    sb.deleteCharAt(sb.length() -1);
                }
                

                sb.append(AIOConstants.SEPERATOR_BYTE_3).append(rqBlog.page);

                 if(rqBlog.page == 0)
                {
                    int countPage = (int)Math.ceil((float)blogSize/(float)rqBlog.size);
                    sb.append(AIOConstants.SEPERATOR_BYTE_1).append(countPage);

                }
                 
            }
            else
            {
            
                if(retSize>0)
                {
                    sb.deleteCharAt(sb.length() -1);
                    sb.append(AIOConstants.SEPERATOR_BYTE_3);
                    sb.append(Integer.toString(blogs.size())).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(Integer.toString(rqBlog.page));

                    //add usersize
                    sb.append(AIOConstants.SEPERATOR_BYTE_3);

                    for(int i = 0; i<userSize; i++ )
                    {
                        UserEntity usrEntity = diffUsers.get(i);
                        sb.append(usrEntity.mUid).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(usrEntity.mUsername).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(usrEntity.avFileId).append(AIOConstants.SEPERATOR_BYTE_2);
                    }

                    sb.deleteCharAt(sb.length() -1);
                }
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
                UserEntity entity = diffUsers.get(i);
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
