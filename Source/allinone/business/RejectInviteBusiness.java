package allinone.business;


import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.protocol.messages.RejectInviteResponse;

public class RejectInviteBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(RejectInviteBusiness.class);
        
	public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
			IResponsePackage aResPkg) {

		
		MessageFactory msgFactory = aSession.getMessageFactory();
		RejectInviteResponse resBoc = (RejectInviteResponse) msgFactory
				.getResponseMessage(aReqMsg.getID());
		try {
                    aSession.setRejectInvite(true);
		    resBoc.mCode = ResponseCode.SUCCESS;
                        
                        
		} finally {
//			aResPkg.addMessage(resBoc);
		}
		return 1;
	}

	
}
