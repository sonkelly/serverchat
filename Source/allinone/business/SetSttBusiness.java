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
import allinone.data.UserEntity;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.SetSttRequest;
import allinone.protocol.messages.SetSttResponse;

public class SetSttBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(SetSttBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[set stt] : Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        SetSttResponse resSocialAvar =
                (SetSttResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try {
            SetSttRequest rqSocialAvar = (SetSttRequest) aReqMsg;
            UserDB userDb = new UserDB();
            userDb.setStt(aSession.getUID(), rqSocialAvar.status);
            if(CacheUserInfo.isUseCache)
            {
                CacheUserInfo cacheUser = new CacheUserInfo();
                UserEntity entity = cacheUser.getUserInfo(aSession.getUID(),aSession.getRealMoney());
                entity.stt = rqSocialAvar.status;
                CacheUserInfo cache = new CacheUserInfo();
                cache.updateCacheUserInfo(entity);
            }
            resSocialAvar.mCode = ResponseCode.SUCCESS;
            
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
