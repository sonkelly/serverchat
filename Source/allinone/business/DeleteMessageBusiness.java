/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;



import org.slf4j.Logger;

import tools.CacheUserInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.databaseDriven.MessageDB;
import allinone.protocol.messages.DeleteMessageRequest;
import allinone.protocol.messages.DeleteMessageResponse;

/**
 *
 * @author mcb
 */
public class DeleteMessageBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(DeleteMessageBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
//        mLog.debug("[BET] : Catch  ; " + aSession.getUserName());
        MessageFactory msgFactory =  aSession.getMessageFactory();
        DeleteMessageResponse resBet = (DeleteMessageResponse) msgFactory
                        .getResponseMessage(aReqMsg.getID());
        
        try {
                DeleteMessageRequest rqAddComment = (DeleteMessageRequest) aReqMsg;
                MessageDB db = new MessageDB();
                db.deleteMessage(rqAddComment.type, rqAddComment.messageId, aSession.getUID());
                
                resBet.mCode = ResponseCode.SUCCESS;
                aSession.write(resBet);
                CacheUserInfo cache = new CacheUserInfo();
                cache.deleteCacheMessage(aSession.getUID());
                
                
        }
        
        catch (Throwable t) {
                //resBet.setFailure(ResponseCode.FAILURE, t.getMessage());
                mLog.error("Process message " + aReqMsg.getID() + " error.", t);
                try {
                    resBet.setFailure(ResponseCode.FAILURE, "Co loi xay ra");
                        aSession.write(resBet);
                } catch (ServerException ex) {
                        // java.util.logging.Logger.getLogger(TurnBusiness.class.getName()).log(Level.SEVERE,
                        // null, ex);
                }

        } 
        
        return 1;
    }
}
