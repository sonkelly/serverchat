/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import tools.CacheBlogInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.BlogEntity;
import allinone.data.ResponseCode;
import allinone.protocol.messages.GetBlogDetailRequest;
import allinone.protocol.messages.GetBlogDetailResponse;
import allinone.queue.data.AuditQueue;



/**
 *
 * @author mcb
 */
public class GetBlogDetailBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetBlogDetailBusiness.class);
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("get blog detail");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetBlogDetailResponse resBlogs = (GetBlogDetailResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
            
            GetBlogDetailRequest rqBlog = (GetBlogDetailRequest) aReqMsg;
            
            //load detail blog if page = 1
            CacheBlogInfo cache= new CacheBlogInfo();
            
            BlogEntity blogEntity =  cache.getBlogDetail(rqBlog.blogId);
            resBlogs.title = blogEntity.getTitle();
            resBlogs.post = blogEntity.getPost();
            resBlogs.blogId = blogEntity.getBlogId();
                
            resBlogs.mCode = ResponseCode.SUCCESS;
            
            aSession.write(resBlogs);
            AuditQueue queue = new AuditQueue();
            queue.insertBlog(blogEntity);
            
        } catch (ServerException ex) {
            resBlogs.setFailure("Có lỗi xảy ra");
            mLog.error(ex.getMessage(), ex);
        } catch (Exception ex) {
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
