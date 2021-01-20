package allinone.business;

import java.util.Vector;

import org.slf4j.Logger;

import tools.CacheUserInfo;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.Room;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.InviteEntity;
import allinone.data.UserEntity;
import allinone.protocol.messages.InviteMXHRequest;
import allinone.protocol.messages.InviteMXHResponse;

public class InviteMXHBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(InviteMXHBusiness.class);
    private static int requestId = 1;
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) {
        int rtn = PROCESS_FAILURE;
        MessageFactory msgFactory = aSession.getMessageFactory();
        InviteMXHResponse resInvite = (InviteMXHResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        InviteMXHResponse resBuddy = (InviteMXHResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        
        mLog.debug("[INVITE]: Catch");
        try {
            InviteMXHRequest rqInvite = (InviteMXHRequest) aReqMsg;
            long sourceID = aSession.getUID();
            long destID = rqInvite.destUid;

            ISession buddySession = aSession.getManager().findSession(destID);
            if(buddySession == null || buddySession.realDead())
            {
                throw new BusinessException("Không tìm thấy người chơi");
            }
            
            if(buddySession.isRejectInvite())
                throw new BusinessException(buddySession.getUserName() +  " không chấp nhận mời chơi nữa");
            
            Vector<Room> joinedRoom = buddySession.getJoinedRooms();

            //if ( buddySession.getCurrentZone() > 0 && buddySession.getCurrentZone() != aSession.getCurrentZone()) {
            if ((buddySession.getCurrentZone() != rqInvite.gameId) && (buddySession.getCurrentZone()>0)) {
                throw new BusinessException("Không mời được. Bạn chơi đang ở Game khác rồi.");

            } 

            if (joinedRoom.size() > 0) {
                throw new BusinessException("Bạn ý đang chơi ở room khác mất rồi");

            } 
            
            
            

            CacheUserInfo cacheUser = new CacheUserInfo();
            UserEntity destUser = cacheUser.getUserInfo(destID,aSession.getRealMoney());
            UserEntity sourceUser = cacheUser.getUserInfo(sourceID,aSession.getRealMoney());
            long minimumMoney = AIOConstants.TIMES * rqInvite.betMoney;
            if(destUser.money <  minimumMoney)
            {
                throw new BusinessException("Người chơi không đủ tiền để tham gia");
            }

            if(sourceUser.money <  minimumMoney)
            {
                throw new BusinessException("Bạn không đủ tiền để mời chơi như vậy");
            }

            


            //room.addWaitingSessionByID(buddySession);
            resInvite.setSuccess("");
            int inviteId = requestId++;
            
            InviteEntity entity = new InviteEntity(rqInvite.gameId, inviteId, rqInvite.betMoney);
            aSession.setInviteEntity(entity); //avoid conflict invitation
            
            aSession.write(resInvite);
            StringBuilder sb = new StringBuilder();
            sb.append(Long.toString(sourceID)).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(sourceUser.mUsername).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(rqInvite.gameId)).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Long.toString(rqInvite.betMoney)).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(inviteId));
            
            resBuddy.setSuccess(sb.toString());
           
            buddySession.write(resBuddy);
                    
               
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
