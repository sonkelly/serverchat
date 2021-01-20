package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.protocol.messages.VivuDisappearResponse;

public class VivuDisappearBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(VivuDisappearBusiness.class);

	@Override
	public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
			IResponsePackage aResPkg) {
		MessageFactory msgFactory = aSession.getMessageFactory();
		VivuDisappearResponse resMove = (VivuDisappearResponse) msgFactory
				.getResponseMessage(aReqMsg.getID());
		mLog.debug("[Vivu Disappear]: Catch");
		try {
			resMove.setSuccess(aSession.getUID());
			aSession.setHide(true);
			aSession.broadcast(resMove, 0);
		} catch (Throwable t) {
			mLog.error("Process message " + aReqMsg.getID() + " error.", t);
			resMove.setFailure("Can not update status!");
			aResPkg.addMessage(resMove);
		}
		return 1;
	}
}
