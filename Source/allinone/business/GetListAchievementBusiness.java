package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.databaseDriven.EventDB;
import allinone.protocol.messages.GetListAchievementResponse;

public class GetListAchievementBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(GetListAchievementBusiness.class);
        
        

	public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
			IResponsePackage aResPkg) {

		
		MessageFactory msgFactory = aSession.getMessageFactory();
		GetListAchievementResponse resBoc = (GetListAchievementResponse) msgFactory
				.getResponseMessage(aReqMsg.getID());
		try {
                    
                    resBoc.mCode = ResponseCode.SUCCESS;
                    resBoc.value = EventDB.getAchivements(aSession.getUserEntity().partnerId);
                        
		} finally {
			aResPkg.addMessage(resBoc);
		}
		return 1;
	}

	
}
