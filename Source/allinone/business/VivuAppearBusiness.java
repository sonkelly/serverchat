package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.MessagesID;
import allinone.protocol.messages.ChooseAreaResponse;
import allinone.protocol.messages.VivuAppearResponse;

import com.allinone.vivu.FuckingPlayer;
import com.allinone.vivu.Group;

public class VivuAppearBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(VivuAppearBusiness.class);

	@Override
	public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
			IResponsePackage aResPkg) {
		MessageFactory msgFactory = aSession.getMessageFactory();
		VivuAppearResponse broadcast = (VivuAppearResponse) msgFactory
				.getResponseMessage(aReqMsg.getID());
		mLog.debug("[Vivu Chat]: Catch");
		try {
			FuckingPlayer user = aSession.getUser();
            broadcast.setSuccess(user.id, user.username, user.getAttr(), user.xPos, user.yPos);
			aSession.setHide(false);
			aSession.broadcast(broadcast, 0);
			Group newGroup = aSession.getGroup();
			ChooseAreaResponse resChoose =
                    (ChooseAreaResponse) msgFactory.getResponseMessage(MessagesID.ChooseArea);
            resChoose.setSuccess(newGroup.getUsers(),
                    newGroup.getmGroupIndex());
            if (!resChoose.users.isEmpty()) {
                aResPkg.addMessage(resChoose);
            }
		} catch (Throwable t) {
			mLog.error("Process message " + aReqMsg.getID() + " error.", t);
			broadcast.setFailure("Can not update status!");
			aResPkg.addMessage(broadcast);
		}
		return 1;
	}
}
