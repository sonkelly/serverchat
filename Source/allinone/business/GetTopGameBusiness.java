/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import java.util.List;

import tools.CacheGameInfo;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.UserEntity;
import allinone.protocol.messages.GetTopGameRequest;
import allinone.protocol.messages.GetTopGameResponse;





/**
 *
 * @author mcb
 */
public class GetTopGameBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetTopGameBusiness.class);
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("get TopGame");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetTopGameResponse resalbum = (GetTopGameResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
            
            GetTopGameRequest rqAlb = (GetTopGameRequest) aReqMsg;
            UserEntity entity = aSession.getUserEntity();
            CacheGameInfo cacheGame = new CacheGameInfo();
            List<UserEntity> lstUsers = cacheGame.getTopGame(entity.partnerId, rqAlb.gameId);
            StringBuilder sb = new StringBuilder();
            int userSize = lstUsers.size();
            
            for(int i = 0; i< userSize; i++)
            {
                UserEntity usrEntity = lstUsers.get(i);
                sb.append(usrEntity.mUid).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(usrEntity.mUsername).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(usrEntity.point).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(usrEntity.playsNumber).append(AIOConstants.SEPERATOR_BYTE_2);
            }
            
            if(userSize>0)
            {
                sb.deleteCharAt(sb.length() -1);
            }
            
            resalbum.setSuccess(sb.toString());
            aResPkg.addMessage(resalbum);
            
        } 
        catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);
            resalbum.setFailure("co loi xay ra");
            aResPkg.addMessage(resalbum);
        }//        catch(BusinessException ex)
//        {
//            mLog.warn(ex.getMessage());
//            resalbum.setFailure(ex.getMessage());
//            
//        }
        finally
        {
//            if (resalbum!= null)
//            {
//                try {
//                    aSession.write(resalbum);
//                } catch (ServerException ex) {
//                    mLog.error(ex.getMessage(), ex);
//                }
//                        
//            }
        }        
        return 1;
    }
}
