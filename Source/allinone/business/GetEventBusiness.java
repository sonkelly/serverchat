package allinone.business;


import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.protocol.messages.GetEventResponse;

public class GetEventBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(GetEventBusiness.class);
        
	public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
			IResponsePackage aResPkg) {

		
		MessageFactory msgFactory = aSession.getMessageFactory();
		GetEventResponse resBoc = (GetEventResponse) msgFactory
				.getResponseMessage(aReqMsg.getID());
		try {
			resBoc.mCode = ResponseCode.SUCCESS;
            resBoc.value = "Event 21->23/9 VUA BÀI Tiến lên miền nam (Số trận thắng nhiều nhất)";
                        
		} finally {
			aResPkg.addMessage(resBoc);
		}
		return 1;
	}

	
}
