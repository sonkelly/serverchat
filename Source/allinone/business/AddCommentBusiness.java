/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;



import org.slf4j.Logger;

import tools.CacheCommentInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.databaseDriven.CommentDB;
import allinone.protocol.messages.AddCommentRequest;
import allinone.protocol.messages.AddCommentResponse;

/**
 *
 * @author mcb
 */
public class AddCommentBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(AddCommentBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
//        mLog.debug("[BET] : Catch  ; " + aSession.getUserName());
        MessageFactory msgFactory =  aSession.getMessageFactory();
        AddCommentResponse resBet = (AddCommentResponse) msgFactory
                        .getResponseMessage(aReqMsg.getID());
        
        try {
                AddCommentRequest rqAddComment = (AddCommentRequest) aReqMsg;
                CommentDB db = new CommentDB();
                if(rqAddComment.systemObjectRecordId < 1)
                    throw new BusinessException("Bình luận sai đối tượng xin mời bạn cập nhập lại");
                
                db.addComment(aSession.getUID(), rqAddComment.comment, rqAddComment.systemObjectRecordId, rqAddComment.systemObjectId);
                resBet.mCode = ResponseCode.SUCCESS;
                aSession.write(resBet);
                CacheCommentInfo cacheComment = new CacheCommentInfo();
                cacheComment.deleteCacheComment(rqAddComment.systemObjectId, rqAddComment.systemObjectRecordId);
//                if(rqAddComment.systemObjectId == AIOConstants.BLOG_SYSTEM_OBJECT_ID)
//                {
//                    CacheBlogInfo
//                }
//                
        }
        
        catch (Throwable t) {
                //resBet.setFailure(ResponseCode.FAILURE, t.getMessage());
                mLog.error("Process message " + aReqMsg.getID() + " error.", t);
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
