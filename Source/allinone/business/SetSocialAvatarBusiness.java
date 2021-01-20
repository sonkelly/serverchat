package allinone.business;

import org.slf4j.Logger;

import tools.CacheUserInfo;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.SetSocialAvatarRequest;
import allinone.protocol.messages.SetSocialAvatarResponse;

public class SetSocialAvatarBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(SetSocialAvatarBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[set social avatar] : Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        SetSocialAvatarResponse resSocialAvar =
                (SetSocialAvatarResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try {
            SetSocialAvatarRequest rqSocialAvar = (SetSocialAvatarRequest) aReqMsg;
            UserDB userDb = new UserDB();
            userDb.setSocialAvatar(aSession.getUID(), rqSocialAvar.fileId, rqSocialAvar.type);
            resSocialAvar.mCode = ResponseCode.SUCCESS;
            
            CacheUserInfo cacheUserInfo = new CacheUserInfo();
            cacheUserInfo.deleteCacheUser(aSession.getUserEntity());
            
        } catch (Exception e) {
            resSocialAvar.setFailure(ResponseCode.FAILURE, "Không thể kết nối đến cư sở dữ liệu");
            mLog.error(e.getMessage(), e);
            
        } finally {
            if ((resSocialAvar != null)) {
                aResPkg.addMessage(resSocialAvar);
            }
        }
        return 1;
    }
}
