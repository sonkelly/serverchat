/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import java.sql.SQLException;
import java.util.List;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.LikeHistoryEntity;
import allinone.data.ResponseCode;
import allinone.databaseDriven.LikeHistoryDB;
import allinone.protocol.messages.GetLikeRequest;
import allinone.protocol.messages.GetLikeResponse;




/**
 *
 * @author mcb
 */
public class GetLikeBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetLikeBusiness.class);
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("get albums");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetLikeResponse resLk = (GetLikeResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
            
            GetLikeRequest rqComment = (GetLikeRequest) aReqMsg;
            
            LikeHistoryDB db = new LikeHistoryDB();
            List<LikeHistoryEntity> lstLks = db.getLike(rqComment.systemObjectId, rqComment.systemObjectRecordId);
            int size = lstLks.size();
            
            StringBuilder sb = new StringBuilder();
            
            for(int i = 0; i< size; i++)
            {
                LikeHistoryEntity entity = lstLks.get(i);
                
                
                sb.append(Long.toString(entity.getUserId())).append(AIOConstants.SEPERATOR_BYTE_1);
                
                sb.append(entity.getUserName()).append(AIOConstants.SEPERATOR_BYTE_2);
                
                
            }
            
            
            
            if(sb.length()>0)
            {
                
                sb.deleteCharAt(sb.length() -1);
            }
            
            resLk.mCode = ResponseCode.SUCCESS;        
            
            resLk.value = sb.toString();
            aSession.write(resLk);
            
            
        } 
        catch (SQLException ex) {
            mLog.error(ex.getMessage(), ex);
        }         
        catch (ServerException ex) {
            mLog.error(ex.getMessage(), ex);
        }
//        catch(BusinessException ex)
//        {
//            mLog.warn(ex.getMessage());
//            resalbum.setFailure(ex.getMessage());
//            
//        }
        finally
        {
            
        }        
        return 1;
    }
}
