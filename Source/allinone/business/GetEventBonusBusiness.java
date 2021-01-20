package allinone.business;
import org.slf4j.Logger;

import tools.CacheEventInfo;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.EventBonusEntity;
import allinone.data.ResponseCode;
import allinone.protocol.messages.GetEventBonusResponse;

public class GetEventBonusBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(GetEventBonusBusiness.class);
        
	public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
			IResponsePackage aResPkg) {
		MessageFactory msgFactory = aSession.getMessageFactory();
		GetEventBonusResponse response = (GetEventBonusResponse) msgFactory.getResponseMessage(aReqMsg.getID());
		response.setSession(aSession);
		response.mCode = ResponseCode.FAILURE;
		mLog.debug(" Get event bonus ");
		try {
			CacheEventInfo cache = new CacheEventInfo();
			EventBonusEntity entity = cache.getEventBonus();
            if (cache.getEventBonus() != null) {
            	response.setSuccess(ResponseCode.SUCCESS, entity.getId(), entity.getImageLink(), entity.getActionLink(), entity.getType());
            } else {
            	response.message = " Hiện tại không có sự kiện nào ";
            }
                        
		} finally {
			aResPkg.addMessage(response);
		}
		
		return 1;
	}
}
