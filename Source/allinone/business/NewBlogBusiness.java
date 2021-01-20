/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;



import org.slf4j.Logger;

import tools.CacheBlogInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.databaseDriven.BlogDB;
import allinone.protocol.messages.NewBlogRequest;
import allinone.protocol.messages.NewBlogResponse;
import java.sql.SQLException;

/**
 *
 * @author mcb
 */
public class NewBlogBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(NewBlogBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
//        mLog.debug("[BET] : Catch  ; " + aSession.getUserName());
        MessageFactory msgFactory =  aSession.getMessageFactory();
        NewBlogResponse resBet = (NewBlogResponse) msgFactory
                        .getResponseMessage(aReqMsg.getID());
        
        try {
                NewBlogRequest rqNewBlog = (NewBlogRequest) aReqMsg;
                BlogDB db = new BlogDB();
                resBet.blogId  = db.newBlog(aSession.getUID(), rqNewBlog.title, rqNewBlog.post);
                resBet.mCode = ResponseCode.SUCCESS;
                               
                aSession.write(resBet);
                
                CacheBlogInfo blogCache = new CacheBlogInfo();
                blogCache.deleteCache(aSession.getUID());
                
                
                
        }
        
        catch (SQLException | ServerException t) {
                //resBet.setFailure(ResponseCode.FAILURE, t.getMessage());
                mLog.error("Process message " + aReqMsg.getID() + " error.", t);
                resBet.message = "Co loi xay ra";
                try {
                        aSession.write(resBet);
                } catch (ServerException ex) {
                        // java.util.logging.Logger.getLogger(TurnBusiness.class.getName()).log(Level.SEVERE,
                        // null, ex);
                }

        } 
        
        return 1;
    }
}
