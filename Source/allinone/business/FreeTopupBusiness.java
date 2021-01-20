package allinone.business;

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
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.FreeTopupResponse;

public class FreeTopupBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(FreeTopupBusiness.class);
   
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) throws ServerException {
        mLog.debug("[Free topup] : Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        FreeTopupResponse resFree = (FreeTopupResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        
        try {
            UserDB db = new UserDB();
            int result = db.freeTopup(aSession.getUID());
            if(result== -1)
            {
                resFree.times = 0;
                throw new BusinessException("Bạn đã hết số lần nạp free");
                
            }
            else if(result== -2)
            {
                resFree.times = 3;
                throw new BusinessException("Số tiền bạn có nhiều hơn so với qui định để nạp Bi miễn phí");
            }
            
            resFree.times = result;
            resFree.mCode = ResponseCode.SUCCESS;
            CacheUserInfo.updateUserCashFromDB(aSession.getUID(), 1000);
            

        } 
        catch(BusinessException ex)
        {
            resFree.setFailure(ResponseCode.FAILURE, ex.getMessage());
        }
        catch (Throwable t) {
            resFree.setFailure(ResponseCode.FAILURE, "Bị lỗi kick out");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally {
            if ((resFree != null)) {
                aResPkg.addMessage(resFree);
            }
        }
        return 1;
    }
}
