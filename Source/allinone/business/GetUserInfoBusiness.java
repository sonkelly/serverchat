package allinone.business;

import java.util.logging.Level;

import org.slf4j.Logger;

import tools.CacheUserInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.databaseDriven.FriendDB;
import allinone.protocol.messages.GetUserInfoRequest;
import allinone.protocol.messages.GetUserInfoResponse;
import java.util.Calendar;

public class GetUserInfoBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetUserInfoBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[GET USER INFOS]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetUserInfoResponse resGetUserInfo = (GetUserInfoResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        aSession.setLastAccessTime(Calendar.getInstance().getTime());
        try {
            GetUserInfoRequest rqGetUserInfo = (GetUserInfoRequest) aReqMsg;
            
            resGetUserInfo.session = aSession;
            long uid = rqGetUserInfo.mUid;
            long source_uid = aSession.getUID();
            mLog.debug("[GET USER INFOS]:" + uid);
//            UserDB userDb = new UserDB();
//            
//            UserEntity user = userDb.getUserInfo(uid);
            CacheUserInfo cacheUser = new CacheUserInfo();
            UserEntity user = cacheUser.getUserInfo(uid,aSession.getRealMoney());
            if (user != null) 
            {
                boolean isFriend = false;
                if (uid == source_uid) {
                    isFriend = true;
                } else {
                    FriendDB friendDb = new FriendDB();
                    int isFriendStatus = friendDb.isFriend(source_uid, uid);
                    if(isFriendStatus == 1){
                        isFriend = true;
                    }
                }
                resGetUserInfo.vipName = user.vipName;
                resGetUserInfo.setSuccess(ResponseCode.SUCCESS, user.mUid, user.mUsername,
                        user.mAge, user.mIsMale, user.money, user.playsNumber, user.avatarID, isFriend, user.level, user.experience);
            } else {// non-existed user
                resGetUserInfo.setFailure(ResponseCode.FAILURE, "Tài khoản này không tồn tại!");
            }

        } catch (Throwable t) {
            resGetUserInfo.setFailure(ResponseCode.FAILURE, "Không thể kết nối đến cơ sở dữ liệu ");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally {
            if ((resGetUserInfo != null) ) {
                try {
                    //aResPkg.addMessage(resGetUserInfo);
                
                    aSession.write(resGetUserInfo);
                } catch (ServerException ex) {
                    java.util.logging.Logger.getLogger(GetUserInfoBusiness.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return 1;
    }
}
