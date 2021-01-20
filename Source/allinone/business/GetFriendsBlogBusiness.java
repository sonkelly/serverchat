/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import java.sql.SQLException;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.databaseDriven.BlogDB;
import allinone.protocol.messages.GetFriendsBlogRequest;
import allinone.protocol.messages.GetFriendsBlogResponse;



/**
 *
 * @author mcb
 */
public class GetFriendsBlogBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetFriendsBlogBusiness.class);
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("get blog account");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetFriendsBlogResponse resBlogs = (GetFriendsBlogResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
            
            GetFriendsBlogRequest rqBlog = (GetFriendsBlogRequest) aReqMsg;
            
            BlogDB db = new BlogDB();
            int[] numBlog =  {0};
            resBlogs.blogs = db.getFriendsBlog(aSession.getUID(), rqBlog.page, numBlog);
            resBlogs.mCode = ResponseCode.SUCCESS;
            if(rqBlog.page== 1)
                resBlogs.numBlog = numBlog[0];
            aSession.write(resBlogs);
            
        } catch (ServerException ex) {
            resBlogs.setFailure("Có lỗi xảy ra");
            mLog.error(ex.getMessage(), ex);
        } catch (SQLException ex) {
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
