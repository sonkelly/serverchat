package allinone.business;

import java.util.Vector;

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
import allinone.protocol.messages.GetFrientListRequest;
import allinone.protocol.messages.GetFrientListResponse;
import allinone.protocol.messages.RequestFriendRequest;
import allinone.protocol.messages.RequestFriendResponse;
import java.util.List;
import vn.game.session.SessionManager;

public class RequestFriendBusiness extends AbstractBusiness {

    private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(RequestFriendBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[GET RequestFriendBusiness]: Catch");
        //aSession.getCollectInfo().append("->GetFrientList: ");
        MessageFactory msgFactory = aSession.getMessageFactory();
        RequestFriendResponse resGetFriendList = (RequestFriendResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        resGetFriendList.session = aSession;
        try {
            long uid = aSession.getUID();
            mLog.debug("[GET FRIENDLIST]: for" + uid);
            FriendDB friendDB = new FriendDB();
            RequestFriendRequest request = (RequestFriendRequest) aReqMsg;
            List<UserEntity> frientlist = friendDB.getRequestFriends(uid, true,request.pageIndex, request.status);

            SessionManager manager = aSession.getManager();
            Vector<UserEntity> res = new Vector<UserEntity>();
            for (UserEntity user : frientlist) {
                ISession buddy = (ISession) manager.findSession(user.mUid);
                if (buddy != null) {
                    user.isLogin = true;
                } else {
                    user.isLogin = false;
                }
                res.add(user);

            }

            resGetFriendList.setSuccess(ResponseCode.SUCCESS, res);
        } catch (Throwable t) {
            resGetFriendList.setFailure(ResponseCode.FAILURE, "Không thể kết nối đến cơ sở dữ liệu ");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally {
            if ((resGetFriendList != null)) {
                aResPkg.addMessage(resGetFriendList);
            }
        }
        return 1;
    }
}
