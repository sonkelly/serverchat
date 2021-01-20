package allinone.business;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import tools.CacheFriendsInfo;
import tools.CacheUserInfo;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.GameDataEntity;
import allinone.data.ResponseCode;
import allinone.data.SocialFriendEntity;
import allinone.data.UserEntity;
import allinone.data.UserInfoEntity;
import allinone.databaseDriven.FriendDB;
import allinone.databaseDriven.PhoneDb;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.GetUserDataRequest;
import allinone.protocol.messages.GetUserDataResponse;

public class GetUserDataBusiness extends AbstractBusiness {

    private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(GetUserDataBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("GetUserDataBusiness: Catch");

        MessageFactory msgFactory = aSession.getMessageFactory();
        GetUserDataResponse response = (GetUserDataResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
            long uid = aSession.getUID();

//            GetUserDataRequest request = (GetUserDataRequest) msgFactory.getRequestMessage(aReqMsg.getID());
            GetUserDataRequest request = (GetUserDataRequest) aReqMsg;

            long dataId = request.userId;

            mLog.debug("[GET GetUserDataBusiness]: for " + dataId);

            List<GameDataEntity> data = new ArrayList<GameDataEntity>();
            
            boolean isFriend = false;
            int friendSize = 0;

            if (uid == dataId) {
                isFriend = true;
                CacheFriendsInfo cacheFriend = new CacheFriendsInfo();
                //List<SocialFriendEntity> lstFriends = cacheFriend.getSocialFriendRequests(uid);
                //friendSize = lstFriends.size();
            } else {
                int isFriendStatus = new FriendDB().isFriend(uid, dataId);
                if(isFriendStatus == 1){
                    isFriend = true;
                }
            }

            // data game
            data = new UserDB().getGameData(dataId);
            
            // get userinfo
//            CacheUserInfo cacheUser = new CacheUserInfo();
//            UserEntity userData = cacheUser.getUserInfo(dataId,aSession.getRealMoney());
            UserDB userObj = new UserDB();
            UserEntity userData = userObj.getUserInfo(dataId);
            
            CacheUserInfo cacheUser = new CacheUserInfo();
            UserEntity entity = cacheUser.getUserInfo(aSession.getUID(),aSession.getRealMoney());
                   

            mLog.debug("[GET GetUserDataBusiness]: friendsize " + friendSize);
            String viewname = userData.viewName;
            if (viewname.equals("")) {
                viewname = userData.mUsername;
            }
            userData.cellPhone = PhoneDb.getPhone(userData.mUid);
//            if (userData.groupId == 1) {//tk fb
//                    userData.avatarID = userData.mUsername;
//                }
            response.setSuccess(ResponseCode.SUCCESS, data, isFriend, userData.money, userData.experience, dataId,
                    viewname, friendSize, userData.mUsername, userData.cmt, userData.cellPhone,userData.avatarID,userData.mail,userData.mIsMale, entity.usrInfoEntity,userData.realmoney);
            response.setSession(aSession);

        } catch (Throwable t) {
            response.setFailure(ResponseCode.FAILURE, "Không thể kết nối đến cơ sở dữ liệu ");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally {
            if ((response != null)) {
                aResPkg.addMessage(response);
            }
        }
        return 1;
    }
}
