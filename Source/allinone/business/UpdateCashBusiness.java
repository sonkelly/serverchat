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
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.UpdateCashResponse;
import java.util.Calendar;
import vn.game.common.SessionUtils;

public class UpdateCashBusiness extends AbstractBusiness {

    private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(UpdateCashBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[GET USER INFOS]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        UpdateCashResponse response = (UpdateCashResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        if (!SessionUtils.checkSessionDB(aSession)) {
            return 0;
        }
        try {
            long uid = aSession.getUID();
             aSession.setLastAccessTime(Calendar.getInstance().getTime());
            response.session = aSession;
            //mLog.debug("[UPDATE CASH INFOS]:" + uid);

            // get data fromDB
            UserDB userDb = new UserDB();
            UserEntity entity = userDb.getUserInfoFullMoney(uid);

            if (entity != null) {

                CacheUserInfo cacheUser = new CacheUserInfo();
                cacheUser.updateCacheUserInfo(entity);

                response.setSuccess(ResponseCode.SUCCESS, entity.mUid, entity.viewName,
                        entity.mAge, entity.mIsMale,entity.realmoney, entity.money, entity.playsNumber, entity.avatarID, entity.level, entity.experience, entity.isActive);

            } else {// non-existed user
                response.setFailure(ResponseCode.FAILURE, "Tài khoản này không tồn tại!");
            }

        } catch (Throwable t) {
            response.setFailure(ResponseCode.FAILURE, "Tài khoản này không tồn tại!");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);

        } finally {
            if ((response != null)) {
                try {
                    aSession.write(response);
                } catch (ServerException ex) {
                    java.util.logging.Logger.getLogger(UpdateCashBusiness.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return 1;
    }
}
