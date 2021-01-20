package allinone.business;

import org.slf4j.Logger;

import tools.CacheFriendsInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.ResponseCode;
import allinone.databaseDriven.FriendDB;
import allinone.protocol.messages.AddSocialFriendRequest;
import allinone.protocol.messages.AddSocialFriendResponse;

public class AddSocialFriendBusiness extends AbstractBusiness {

    private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(AddSocialFriendBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[Add social Friend] : Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        AddSocialFriendResponse resAddFriend
                = (AddSocialFriendResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try {
            AddSocialFriendRequest rqAddFriend = (AddSocialFriendRequest) aReqMsg;
            FriendDB friendDb = new FriendDB();
            int status = FriendDB.STATUS_FRIEND_NOT_APPROVED;
            if (rqAddFriend.isConfirmed) {
                status = FriendDB.STATUS_FRIENDED;
            }
//            int addResult = friendDb.updateRequestFriend(status, aSession.getUID(), rqAddFriend.friendID,aSession.getRealMoney());
            int addResult = friendDb.approvedFriend(status, aSession.getUID(), rqAddFriend.friendID, aSession.getRealMoney());
            if (rqAddFriend.friendID < 1) {
                throw new BusinessException("Không tồn tại người chơi");
            }

            resAddFriend.setSuccess(ResponseCode.SUCCESS);
            resAddFriend.value = status + "";

            //if (rqAddFriend.isConfirmed) {
            switch (addResult) {
                case FriendDB.STATUS_FRIENDED:
                    resAddFriend.setFailure(ResponseCode.FAILURE, "Hai bạn đã là bạn của nhau rồi");
                    break;
          
                case FriendDB.STATUS_FRIEND_REQUESTING:
                    resAddFriend.setFailure(ResponseCode.FAILURE, "Bạn đã ở danh sách yêu cầu kết bạn của bạn đó");
                    break;
                case FriendDB.STATUS_FRIEND_NOT_APPROVED:
                    resAddFriend.setFailure(ResponseCode.FAILURE, "Đã hủy yêu cầu kết bạn ");
                    break;
                case FriendDB.STATUS_FRIEND_REQUEST:
                    break;
                default:
                    resAddFriend.setFailure(ResponseCode.FAILURE, "Đã gửi yêu cầu thành công ");
                    break;
            }
        } catch (BusinessException e) {
            resAddFriend.setFailure(ResponseCode.FAILURE, e.getMessage());
        } catch (Exception e) {
            resAddFriend.setFailure(ResponseCode.FAILURE, "Không thể kết nối đến cư sở dữ liệu");
        } finally {
            if ((resAddFriend != null)) {
                aResPkg.addMessage(resAddFriend);
            }
        }
        return 1;
    }
}
