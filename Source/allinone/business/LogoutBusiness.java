package allinone.business;

import java.sql.SQLException;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.MessagesID;
import allinone.databaseDriven.SessionDB;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.CancelRequest;

public class LogoutBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(LogoutBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {

        try {

            mLog.debug("Logout session");
            aSession.setLoggedIn(false);

            UserDB userDb = new UserDB();

            mLog.debug(" Logout db ");

            userDb.logout(aSession.getUID(), aSession.getCollectInfo().toString());
            //add by zep
            try {
                SessionDB sdb = new SessionDB();
                sdb.addSessionDB(aSession.getUID(), "", "", "", "");
            } catch (Exception e) {

            }
            //end by zep
//            CacheUserInfo cacheUser = new CacheUserInfo();
//            UserEntity entity = cacheUser.getUserInfo(aSession.getUID());
//            entity.isLogin = false;
//            entity.isOnline = false;
//            cacheUser.updateCacheUserInfo(entity);            
//            aSession.setUserName("");

            mLog.debug("Logout Room");
            if (aSession.getRoom() != null && aSession.getRoom().getAttactmentData() != null
                    && aSession.getRoom().getAttactmentData().getNotNullSession() != null) {

                IResponsePackage responsePkg = aSession.getDirectMessages();
                MessageFactory msgFactory = aSession.getMessageFactory();
                long matchID = aSession.getRoom().getRoomId();

                if (msgFactory == null) {
                    return 1;
                }

                // Case
                IBusiness business = msgFactory.getBusiness(MessagesID.MATCH_CANCEL);
                CancelRequest rqMatchCancel = (CancelRequest) msgFactory.getRequestMessage(MessagesID.MATCH_CANCEL);
                rqMatchCancel.uid = aSession.getUID();
                rqMatchCancel.mMatchId = matchID;
                rqMatchCancel.isLogout = true;

                try {
                    business.handleMessage(aSession, rqMatchCancel, responsePkg);
                } catch (ServerException se) {
                    this.mLog.error("Exception Catch Error!", se.getCause());
                }

            }

            mLog.debug("Logout Group chat");
//            try
//            {
//                aSession.getManager().removePrvChatSession(aSession.getUID());
//                if (aSession.isMXHDevice() && aSession.getGroup() != null) {
//                    aSession.setmGroup(null);
////                    MessageFactory msgFactory = aSession.getMessageFactory();
////                    VivuDisappearResponse disA =
////                            (VivuDisappearResponse) msgFactory.getResponseMessage(MessagesID.ViVuDisappear);
////                    disA.setSuccess(aSession.getUID());
//                    try {
////                        aSession.getGroup().broadcast(disA, false, aSession.getUID());
//                        aSession.getGroup().left(aSession);
//                    } catch (BusinessException ex) {
//                        mLog.error(ex.getMessage(), ex.getMessage());
//                    } 
//                }
//            }
//            catch(Exception ex)
//            {
//                if(ex!=null)
//                    mLog.error(ex.getMessage(), ex.getMessage());
//            }
        } catch (SQLException ex) {
            mLog.debug("Exception logout session", ex.getMessage());
        }
        return 1;

    }
}
