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
import allinone.databaseDriven.LikeHistoryDB;
import allinone.protocol.messages.InsertLikeRequest;
import allinone.protocol.messages.InsertLikeResponse;

/**
 *
 * @author mcb
 */
public class InsertLikeBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(InsertLikeBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
//        mLog.debug("[BET] : Catch  ; " + aSession.getUserName());
        MessageFactory msgFactory =  aSession.getMessageFactory();
        InsertLikeResponse resBet = (InsertLikeResponse) msgFactory
                        .getResponseMessage(aReqMsg.getID());
        
        try {
                InsertLikeRequest rqAddComment = (InsertLikeRequest) aReqMsg;
                LikeHistoryDB db = new LikeHistoryDB();
                int ret = db.insertLike(aSession.getUID(), rqAddComment.systemObjectRecordId, rqAddComment.systemObjectId);
                if(ret == -1)
                {
                    resBet.mCode = ResponseCode.FAILURE;
                    resBet.mErrorMsg = "Bạn đã like rồi";
                }
                else
                {
                    resBet.mCode = ResponseCode.SUCCESS;
                    
                    
                }
                
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
