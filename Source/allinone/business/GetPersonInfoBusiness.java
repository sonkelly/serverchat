package allinone.business;

import org.slf4j.Logger;

import tools.CacheUserInfo;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.UserEntity;
import allinone.data.UserInfoEntity;
import allinone.protocol.messages.GetPersonInfoRequest;
import allinone.protocol.messages.GetPersonInfoResponse;

public class GetPersonInfoBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetPersonInfoBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[GET USER INFOS]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetPersonInfoResponse resGetUserInfo = (GetPersonInfoResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try {
            GetPersonInfoRequest rqGetUserInfo = (GetPersonInfoRequest) aReqMsg;
            long uid = rqGetUserInfo.uid;
            
//            UserDB db = new UserDB();
            CacheUserInfo cache = new CacheUserInfo();
            UserEntity userEntity = cache.getFullUserInfo(uid,aSession.getRealMoney());
                    
             //db.getUserMxhInfo(uid);
            if (userEntity != null) 
            {
                UserInfoEntity user =userEntity.usrInfoEntity;
                StringBuilder sb = new StringBuilder();
                sb.append(user.cityId).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(user.address==null ? "":user.address).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(user.jobId ).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(user.birthDay==null?"" :user.birthDay.getTime()).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(user.hobby==null ? "":user.hobby).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(user.nickSkype==null ? "":user.nickSkype).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(user.nickYahoo==null ? "":user.nickYahoo).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(user.phoneNumber==null ? "":user.phoneNumber).append(AIOConstants.SEPERATOR_BYTE_1);
                //sb.append(user.characterId).append(AIOConstants.SEPERATOR_BYTE_1);  
                sb.append(userEntity.money);
                resGetUserInfo.setSuccess(sb.toString());
                
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
