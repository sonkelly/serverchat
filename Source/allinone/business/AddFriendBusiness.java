package allinone.business;

import allinone.data.MessagesID;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.databaseDriven.FriendDB;
import static allinone.databaseDriven.FriendDB.STATUS_FRIENDED;
import allinone.protocol.messages.AddFriendRequest;
import allinone.protocol.messages.AddFriendResponse;
import allinone.protocol.messages.RequestFriendRequest;
import allinone.server.Server;
import java.util.logging.Level;
import vn.game.common.ServerException;
import vn.game.protocol.IBusiness;
import vn.game.session.SessionManager;

public class AddFriendBusiness extends AbstractBusiness {

    private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(AddFriendBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[Add Friend] : Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        AddFriendResponse resAddFriend
                = (AddFriendResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try {
            AddFriendRequest rqAddFriend = (AddFriendRequest) aReqMsg;
            FriendDB friendDb = new FriendDB();
            int addResult = friendDb.addFriend(aSession.getUID(), rqAddFriend.friendID, aSession.getRealMoney());
            resAddFriend.session = aSession;
            switch (addResult) {
                case FriendDB.STATUS_FRIEND_REQUEST:
                    resAddFriend.setSuccess(ResponseCode.SUCCESS);
                    resAddFriend.mErrorMsg = "Đã gửi kết bạn thành công.";
                    //tim dang dang online thi gui messsage cho nguoi nay luon
                    this.sendMsgtoUserFriendRequest(aSession, rqAddFriend.friendID);
                    //end 
                    break;
                case FriendDB.STATUS_FRIEND_REQUESTING:
                    resAddFriend.setFailure(ResponseCode.FAILURE, "Bạn đã gửi lời kết bạn rồi");
                    break;
                case FriendDB.STATUS_FRIENDED:
                    resAddFriend.setFailure(ResponseCode.FAILURE, "Hai bạn đã là bạn của nhau rồi");
                    break;
                default:
                    resAddFriend.setFailure(ResponseCode.FAILURE, "Không thể gửi lời mời");
                    break;
            }

        } catch (Exception e) {
            resAddFriend.setFailure(ResponseCode.FAILURE, "Không thể kết nối đến cư sở dữ liệu");
        } finally {
            if ((resAddFriend != null)) {
                aResPkg.addMessage(resAddFriend);
            }
        }
        return 1;
    }

    public void sendMsgtoUserFriendRequest(ISession aSession, long userId) {
       // mLog.debug("sendMsgtoUserFriendRequest userId:" + userId);
//        SessionManager sm = Server.getWorker().getmSessionMgr();
//        ISession temp = sm.findSession(userId);
        try {
            SessionManager manager = aSession.getManager();
            ISession temp = (ISession) manager.findSession(userId);
            mLog.debug("temp:" );
            if (temp != null && !temp.isExpired() && temp.isLoggedIn()) {
                // Kiem tra xem co dang ton tai trong ban choi game nao khong
                //mLog.debug("ok dan online tra ve user");
                IBusiness business = temp.getMessageFactory().getBusiness(MessagesID.FRIEND_LIST_REQUEST);
                RequestFriendRequest rqFriend = (RequestFriendRequest) temp.getMessageFactory().getRequestMessage(MessagesID.FRIEND_LIST_REQUEST);
                rqFriend.pageIndex = 1;

                try {
                    business.handleMessage(temp, rqFriend, temp.getDirectMessages());
                } catch (ServerException ex) {
                    java.util.logging.Logger.getLogger(ChatBusiness.class.getName()).log(Level.SEVERE, null, ex);
                }

            }else{
                mLog.debug("ko tim thay session");
            }
        } catch (Exception e) {
            mLog.debug("error:"+e.getMessage());
        }
    }
}
