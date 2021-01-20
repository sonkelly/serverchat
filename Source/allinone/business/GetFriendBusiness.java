package allinone.business;

import java.util.Vector;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.UserEntity;
import allinone.databaseDriven.FriendDB;
import allinone.protocol.messages.GetFriendRequest;
import allinone.protocol.messages.GetFriendResponse;

public class GetFriendBusiness extends AbstractBusiness {

    private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(GetFriendBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[GET FRIENDLIST]: Catch");

        MessageFactory msgFactory = aSession.getMessageFactory();
        GetFriendResponse resGetFriendList = (GetFriendResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
            long uid = aSession.getUID();
            mLog.debug("[GET FRIENDLIST]: for" + uid);
            FriendDB friendDB = new FriendDB();
//             Vector<UserEntity> frientlist = friendDB.getFrientList(uid, false);
//             
//            StringBuilder sb = new StringBuilder();
//            int size = frientlist.size();
//            for(int i = 0; i< size; i++)
//            {
//                UserEntity entity = frientlist.get(i);
//                sb.append(entity.mUid).append(AIOConstants.SEPERATOR_BYTE_1);
//                sb.append(entity.mUsername).append(AIOConstants.SEPERATOR_BYTE_1);
//                sb.append(entity.isLogin?"1":"0").append(AIOConstants.SEPERATOR_BYTE_2);
//            }

//            if(size>0)
//                sb.deleteCharAt(sb.length() -1);
//            
//             resGetFriendList.setSuccess(sb.toString());
            GetFriendRequest request = (GetFriendRequest) aReqMsg;
            boolean isFriend = false;
            int isFriendStatus = friendDB.isFriend(uid, request.userId);
            mLog.debug("isFriendStatus:"+isFriendStatus);
            if (isFriendStatus == 1) {
                isFriend = true;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(request.userId).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(isFriend ? "1" : "0");
            resGetFriendList.setSuccess(sb.toString());
            mLog.debug("sb.toString():"+sb.toString());
        } catch (Throwable t) {
            resGetFriendList.setFailure("Có lỗi xảy ra");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally {
            if ((resGetFriendList != null)) {
                aResPkg.addMessage(resGetFriendList);
            }
        }
        return 1;
    }
}
