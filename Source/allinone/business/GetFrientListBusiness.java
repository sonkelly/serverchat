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
import java.sql.SQLException;
import java.util.logging.Level;
import vn.game.session.SessionManager;

public class GetFrientListBusiness extends AbstractBusiness {

    private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(GetFrientListBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[GET FRIENDLIST]: Catch");
        aSession.getCollectInfo().append("->GetFrientList: ");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetFrientListResponse resGetFriendList = (GetFrientListResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        resGetFriendList.session = aSession;
        try {
            long uid = aSession.getUID();
            mLog.debug("[GET FRIENDLIST]: for" + uid);
            FriendDB friendDB = new FriendDB();
            GetFrientListRequest request = (GetFrientListRequest) aReqMsg;
            Vector<UserEntity> res = new Vector<UserEntity>();
            if (request.status == 3) {
                res = this.getFriendOnline(uid, aSession);
            } else {
                Vector<UserEntity> frientlist = friendDB.getFrientList(uid, true, request.pageIndex, request.status);

                SessionManager manager = aSession.getManager();

                for (UserEntity user : frientlist) {
                    ISession buddy = (ISession) manager.findSession(user.mUid);
                    if (buddy != null ) {
                        user.isLogin = true;
                    } else {
                        user.isLogin = false;
                    }
//					add những người offline và không phải là chính mình
                    if (buddy == null || buddy.getUID() != uid) {
                        res.add(user);
                    }

                }
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

    public Vector<UserEntity> getFriendOnline(long uid, ISession aSession) {
        Vector<UserEntity> res = new Vector<UserEntity>();
        FriendDB friendDB = new FriendDB();
        Vector<UserEntity> frientlist = null;
        try {
            frientlist = friendDB.getFrientList(uid, true, 1, 1);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(GetFrientListBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        SessionManager manager = aSession.getManager();
        for (UserEntity user : frientlist) {
            ISession buddy = (ISession) manager.findSession(user.mUid);
            if (buddy != null) {
                user.isLogin = true;
            } else {
                user.isLogin = false;
            }
            res.add(user);

        }
        return res;
    }
}
