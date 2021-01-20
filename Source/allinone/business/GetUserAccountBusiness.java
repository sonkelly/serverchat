package allinone.business;

import org.slf4j.Logger;

import tools.CacheUserInfo;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.MessagesID;
import allinone.data.UserEntity;
import allinone.protocol.messages.GetSocialAvatarRequest;
import allinone.protocol.messages.GetUserAccountRequest;
import allinone.protocol.messages.GetUserAccountResponse;

public class GetUserAccountBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetUserAccountBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[GET USER Account]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetUserAccountResponse resGetUserInfo = (GetUserAccountResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try {
            GetUserAccountRequest rqGetUserInfo = (GetUserAccountRequest) aReqMsg;
            long uid = rqGetUserInfo.uid;
            
//            UserDB db = new UserDB();
           CacheUserInfo cacheUser = new CacheUserInfo();
            UserEntity user = cacheUser.getUserInfo(uid,aSession.getRealMoney());
            if (user != null) 
            {
                StringBuilder sb = new StringBuilder();
                sb.append(user.mUid).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(user.stt==null?"":user.stt).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(user.avFileId);
                
                resGetUserInfo.setSuccess(sb.toString());
                if(user.avFileId >0)
                {
                    GetSocialAvatarRequest rqSocial = (GetSocialAvatarRequest) msgFactory
						.getRequestMessage(MessagesID.GET_SOCIAL_AVATAR);
                    
                    rqSocial.type = 0;
                    rqSocial.uid = user.mUid;
                    IResponsePackage responsePkg = aSession.getDirectMessages();
                    
                    IBusiness business = msgFactory.getBusiness(MessagesID.GET_SOCIAL_AVATAR);
                    business.handleMessage(aSession, rqSocial, responsePkg);
                    
                }
               
                
                
            } else {
                resGetUserInfo.setFailure("Tài khoản này không tồn tại!");
            }

        } catch (Throwable t) {
            resGetUserInfo.setFailure("Có lỗi xảy ra");
            
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally {
            if(resGetUserInfo != null)
                aResPkg.addMessage(resGetUserInfo);
        }

        return 1;
    }
}
