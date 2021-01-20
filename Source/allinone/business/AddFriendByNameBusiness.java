package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.databaseDriven.FriendDB;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.AddFriendByNameRequest;
import allinone.protocol.messages.AddFriendByNameResponse;

public class AddFriendByNameBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(AddFriendByNameBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) {
        mLog.debug("[Add Friend] : Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        AddFriendByNameResponse resAddFriend = (AddFriendByNameResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try {
            resAddFriend.session = aSession;
            
            AddFriendByNameRequest rqAddFriend = (AddFriendByNameRequest) aReqMsg;
            UserDB userDb = new UserDB();
            UserEntity user = userDb.getUserInfo(rqAddFriend.friendName, aSession.getRealMoney());
            long currID = aSession.getUID();
            FriendDB friendDb = new FriendDB();
            int addResult = friendDb.addFriend(currID, user.mUid, aSession.getRealMoney());
            if(user ==  null)
            {
                resAddFriend.setFailure(ResponseCode.FAILURE,
                    "Không tìm được tên bạn ấy");
            }
            else
            {
                if(addResult == 0)
                {
                     resAddFriend.setSuccess(ResponseCode.SUCCESS, user);
                }
                else
                {
                     resAddFriend.setFailure(ResponseCode.FAILURE,
                            "Hai bạn đã là bạn của nhau rồi");
                }
            }
            

          
        } catch (Exception e) {
            
            mLog.error(e.getMessage());
            resAddFriend.setFailure(ResponseCode.FAILURE,
                    "Không tìm được tên bạn ấy");
        } finally {
            if ((resAddFriend != null)) {
                aResPkg.addMessage(resAddFriend);
            }
        }
        return 1;
    }
}
