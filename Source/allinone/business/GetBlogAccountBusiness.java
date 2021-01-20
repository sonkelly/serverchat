/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tools.CacheBlogInfo;
import tools.CacheFriendsInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.BlogEntity;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.protocol.messages.GetBlogAccountRequest;
import allinone.protocol.messages.GetBlogAccountResponse;



/**
 *
 * @author mcb
 */
public class GetBlogAccountBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetBlogAccountBusiness.class);
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("get friends blog");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetBlogAccountResponse resBlogs = (GetBlogAccountResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
            
            GetBlogAccountRequest rqBlog = (GetBlogAccountRequest) aReqMsg;
            
            CacheBlogInfo cacheInfo = new CacheBlogInfo();
            
            long userId = rqBlog.userId;    
            int roleId = AIOConstants.PUBLIC_ROLE;
            
            
            
            if(userId == aSession.getUID())
            {
                roleId = AIOConstants.ONLY_ME_ROLE;
            }
            else
            {
                CacheFriendsInfo cacheFriend = new CacheFriendsInfo();
                
                List<UserEntity> lstFriends = cacheFriend.getFriends(userId);
                int friendSize = lstFriends.size();
                for(int i = 0; i< friendSize; i++)
                {
                    if(lstFriends.get(i).mUid == userId)
                    {
                        roleId = AIOConstants.ONLY_FRIENDS_ROLE;
                        break;
                    }
                }
            }
            
            int pageSize = rqBlog.size;
            if(pageSize == 0)
            {
                pageSize = AIOConstants.PAGE_DEFAULT_SIZE;
            }
            
            
            List<BlogEntity> blogs= cacheInfo.getBlogAccount(userId, roleId);
            int fromIndex = rqBlog.page * pageSize;
            int toIndex = fromIndex + pageSize;
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
            int retSize = retBlog.size();
            for(int i = 0; i< retSize; i++)
            {
                BlogEntity entity = retBlog.get(i);
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
                    sb.append(rqBlog.page);
                     if(rqBlog.page == 0)
                     {
                        int countPage = (int)Math.ceil((float)blogSize/(float)pageSize);
                        sb.append(AIOConstants.SEPERATOR_BYTE_1).append(countPage);

                     }

                }
//                sb.deleteCharAt(sb.length() -1);

//                sb.append(AIOConstants.SEPERATOR_BYTE_3).append(rqBlog.page);

                
                 
            }
            else
            {
                if(retSize>0)
                {
                    sb.deleteCharAt(sb.length() -1);
                    sb.append(AIOConstants.SEPERATOR_BYTE_3);
                    sb.append(Integer.toString(blogs.size())).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(Integer.toString(rqBlog.page));
                }
            }
            
            resBlogs.value = sb.toString();
            resBlogs.mCode = ResponseCode.SUCCESS;
//            if(rqBlog.page== 1)
//                resBlogs.numBlog = numBlog[0];
            
            aSession.write(resBlogs);
            
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
