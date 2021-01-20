package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.protocol.messages.GetMSTTResponse;

public class GetMSTTBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(GetMSTTBusiness.class);
        
	public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
			IResponsePackage aResPkg) {

		
		MessageFactory msgFactory = aSession.getMessageFactory();
		GetMSTTResponse resBoc = (GetMSTTResponse) msgFactory
				.getResponseMessage(aReqMsg.getID());
		try {
			resBoc.mCode = ResponseCode.SUCCESS;
                        StringBuilder sb = new StringBuilder();
//                        sb.append("12345").append(AIOConstants.SEPERATOR_NEW_ELEMENT);
//                        sb.append("03-10-2012").append(AIOConstants.SEPERATOR_NEW_ARRAY);
//                        sb.append("12346").append(AIOConstants.SEPERATOR_NEW_ELEMENT);
//                        sb.append("03-10-2012");
                        resBoc.value = sb.toString();
                        
		} finally {
			aResPkg.addMessage(resBoc);
		}
		return 1;
	}

	
}
