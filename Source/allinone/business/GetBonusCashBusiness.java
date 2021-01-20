package allinone.business;
import java.util.logging.Level;

import org.slf4j.Logger;

import tools.CacheEventInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.EventBonusEntity;
import allinone.data.ResponseCode;
import allinone.databaseDriven.EventDB;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.GetBonusCashRequest;
import allinone.protocol.messages.GetBonusCashResponse;

public class GetBonusCashBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetBonusCashBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[GET USER INFOS]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetBonusCashResponse response = (GetBonusCashResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        GetBonusCashRequest request = (GetBonusCashRequest) aReqMsg;
        
        try {
        	long uid = aSession.getUID();
        	response.session = aSession;
            mLog.debug("[GET BONUS CASH]:" + uid  + request.getType() + request.getSocialId() + request.getDeviceUid() + request.getEventKey());
                                    
            EventDB eventdb = new EventDB();
            UserDB userDb = new UserDB();
            EventBonusEntity entity = null;
            
            int result = 0;
            if(eventdb.EVENT_BONUS_TYPE_FACESHARE == request.getType()) {
            	entity = eventdb.getEventBonusEntityByType(request.getType());
            	
            	if(entity == null) {
            		response.setFailure(ResponseCode.FAILURE, " Sự kiện đã hết hạn, vui lòng chờ sự kiện mới!");
            	} else {
            		result = userDb.updateUserMoneyForBonus(uid, entity.getBonus(), entity.getId(), entity.getType(), request.getDeviceUid(), request.getSocialId());
            		if(result == 0) {
            			response.setSuccess(ResponseCode.SUCCESS, entity.getBonus(), "Cảm ơn bạn đã Share, Bạn được tặng thưởng " + entity.getBonus() + " bi");
            		} else if (result == 1) {
            			response.setSuccess(ResponseCode.FAILURE, entity.getBonus(), "Cảm ơn bạn đã Share, Mỗi tài khoản chỉ được tặng thưởng 1 lần ");
            		} else if (result == 2) {
            			response.setSuccess(ResponseCode.FAILURE, entity.getBonus(), "Cảm ơn bạn đã Share, Bạn đã được tặng thưởng rồi");
            		} else if (result == 3) {
            			response.setSuccess(ResponseCode.FAILURE, entity.getBonus(), "Cảm ơn bạn đã Share, trên mỗi máy chỉ được tặng thưởng 1 lần ");
            		}
            	}
            	
            } else {
            	CacheEventInfo cache = new CacheEventInfo();
            	entity = cache.getEventBonus();

            	if(entity == null) {
            		response.setFailure(ResponseCode.FAILURE, " Sự kiện đã hết hạn, vui lòng chờ sự kiện mới!");
            	} else {
            		if(eventdb.EVENT_BONUS_TYPE_BAONETVOTE == request.getType() && !entity.getEventKey().equals(request.getEventKey())) {
            			response.setFailure(ResponseCode.FAILURE, " Mã thưởng không đúng, vui lòng nhập lại!");            			                		            			
            		} else {
            			result = userDb.updateUserMoneyForBonus(uid, entity.getBonus(), entity.getId(), entity.getType(), request.getDeviceUid(), request.getSocialId());
                		mLog.debug(" Bonus event result " + result);
                		
            			if(result == 0) {
                			response.setSuccess(ResponseCode.SUCCESS, entity.getBonus(), " Bạn được tặng thưởng " + entity.getBonus() + " bi");
                		} else if (result == 1) {
                			response.setSuccess(ResponseCode.FAILURE, entity.getBonus(), " Mỗi tài khoản chỉ được tặng thưởng 1 lần ");
                		} else if (result == 2) {
                			response.setSuccess(ResponseCode.FAILURE, entity.getBonus(), " Bạn đã được tặng thưởng rồi");
                		} else if (result == 3) {
                			response.setSuccess(ResponseCode.FAILURE, entity.getBonus(), " Trên mỗi máy chỉ được tặng thưởng 1 lần ");
                		}
            		}

            	}
            }	
        } catch (Throwable t) {
        	response.setFailure(ResponseCode.FAILURE, "Thông tin không chính xác, bạn không được tặng thưởng");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
            
        } finally {
            if ((response != null) ) {
                try {
                    aSession.write(response);
                } catch (ServerException ex) {
                    java.util.logging.Logger.getLogger(GetBonusCashBusiness.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return 1;
    }
}
