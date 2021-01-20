package allinone.business;

import org.slf4j.Logger;

import tools.CacheMatch;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.Room;
import vn.game.session.ISession;
import allinone.data.MatchEntity;
import allinone.data.MessagesID;
import allinone.protocol.messages.EnterFriendTableRequest;
import allinone.protocol.messages.EnterFriendTableResponse;

public class EnterFriendTableBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(EnterFriendTableBusiness.class);
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) {
        int rtn = PROCESS_FAILURE;
        MessageFactory msgFactory = aSession.getMessageFactory();
        EnterFriendTableResponse resInvite = (EnterFriendTableResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        
        
        mLog.debug("[INVITE]: Catch");
        try {
            EnterFriendTableRequest rqInvite = (EnterFriendTableRequest) aReqMsg;
            
            long destID = rqInvite.friendId;

            ISession buddySession = aSession.getManager().findSession(destID);
            if(buddySession == null || buddySession.realDead())
            {
                throw new BusinessException("Bạn của bạn đã ofline rồi");
            }
            
            Room room = buddySession.getRoom();
            
            if(room == null)
            {
                throw new BusinessException("Bạn của bạn đang không chơi game nào cả");
            }
            
            MatchEntity matchEntity = CacheMatch.getMatch(room.getRoomId());
            
            if(matchEntity == null)
            {
                throw new BusinessException("Bạn của bạn đang không chơi game nào cả");
            }
            
            if(room.getAttactmentData().isFullTable())
            {
                throw new BusinessException("Bàn chơi bạn của bạn đã đầy rồi");
            }
           
            
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(matchEntity.getZoneId()));
            resInvite.setSuccess(sb.toString());
            
            aSession.write(resInvite);
            
           
            aSession.setChatRoom(0);
                        
            IResponsePackage joinResPkg = aSession.getDirectMessages();
            IBusiness joinBusiness = msgFactory
						.getBusiness(MessagesID.MATCH_JOIN);
       
            aSession.setCurrentZone(matchEntity.getZoneId());
            
               
        }
        catch(BusinessException be)
        {
            
            resInvite.setFailure(be.getMessage());
            aResPkg.addMessage(resInvite);
        }
        catch (Throwable t) {
            resInvite.setFailure("Không thực hiện mời được!");
            //aSession.setLoggedIn(false);
            aResPkg.addMessage(resInvite);
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        }
        return rtn;
    }
}
