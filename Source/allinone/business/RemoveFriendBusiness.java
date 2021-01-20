package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.databaseDriven.FriendDB;
import allinone.protocol.messages.RemoveFriendRequest;
import allinone.protocol.messages.RemoveFriendResponse;

public class RemoveFriendBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(RemoveFriendBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[Remove Friend] : Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        RemoveFriendResponse resRemoveFriend =
                (RemoveFriendResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try {
            RemoveFriendRequest rqRemoveFriend = (RemoveFriendRequest) aReqMsg;
            FriendDB friendDb = new FriendDB();
            friendDb.removeFriend(rqRemoveFriend.currID, rqRemoveFriend.friendID);
            resRemoveFriend.setSuccess(ResponseCode.SUCCESS);
//            if(DatabaseDriver.isFriend(rqRemoveFriend.currID, rqRemoveFriend.friendID)) {
//	            DatabaseDriver.removeFriend(rqRemoveFriend.currID, rqRemoveFriend.friendID);
//	            DatabaseDriver.removeFriend(rqRemoveFriend.friendID, rqRemoveFriend.currID);
//	            resRemoveFriend.setSuccess(ResponseCode.SUCCESS);
//            } else {
//            	resRemoveFriend.setFailure(ResponseCode.FAILURE, "Bạn đã thêm bạn ý vào danh sách bạn chưa nhỉ?");
//            }

        } catch (Exception e) {
            resRemoveFriend.setFailure(ResponseCode.FAILURE, "Không thể kết nối đến cơ sở dữ liệu");
//            mLog.debug("Loi: "+e.getCause());
        } finally {
            if ((resRemoveFriend != null)) {
                aResPkg.addMessage(resRemoveFriend);
            }
        }
        return 1;
    }
}
