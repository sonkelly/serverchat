package allinone.business;


import java.util.ArrayList;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.protocol.messages.GetTournementListResponse;
import allinone.tournement.TournementEntity;

public class GetTournementListBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(GetTournementListBusiness.class);

        @Override
	public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
			IResponsePackage aResPkg) {
		MessageFactory msgFactory = aSession.getMessageFactory();
		GetTournementListResponse resTourInfo = (GetTournementListResponse) msgFactory
				.getResponseMessage(aReqMsg.getID());
		try {
			ArrayList<TournementEntity> res = aSession.getTourMgr().getToursInfo(aSession.getUID());
			if(res.size() > 0) {
				resTourInfo.setSuccess(res);
			} else {
				resTourInfo.setFailure("Hiện tại chưa có giải đấu nào");
			}
			aSession.write(resTourInfo);
		} catch (Throwable t) {
			mLog.error("Process message " + aReqMsg.getID() + " error.", t.getMessage());
			try {
				aSession.write(resTourInfo);
			} catch (ServerException ex) {
			}
		}
		return 1;
	}
}
