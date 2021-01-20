/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;



import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.databaseDriven.CommentDB;
import allinone.protocol.messages.UpdateCommentRequest;
import allinone.protocol.messages.UpdateCommentResponse;

/**
 *
 * @author mcb
 */
public class UpdateCommentBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(UpdateCommentBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
//        mLog.debug("[BET] : Catch  ; " + aSession.getUserName());
        MessageFactory msgFactory =  aSession.getMessageFactory();
        UpdateCommentResponse resBet = (UpdateCommentResponse) msgFactory
                        .getResponseMessage(aReqMsg.getID());
        
        try {
                UpdateCommentRequest rqAddComment = (UpdateCommentRequest) aReqMsg;
                CommentDB db = new CommentDB();
                db.updateComment( rqAddComment.comment, rqAddComment.commentId);
                resBet.mCode = ResponseCode.SUCCESS;
                aSession.write(resBet);
                
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
