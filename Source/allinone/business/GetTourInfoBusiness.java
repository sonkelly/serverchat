package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.protocol.messages.GetTourInfoRequest;
import allinone.protocol.messages.GetTourInfoResponse;

public class GetTourInfoBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(GetTourInfoBusiness.class);

	public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
			IResponsePackage aResPkg) {

		mLog.debug("[Get Tour Info]: Catch");
		MessageFactory msgFactory = aSession.getMessageFactory();
		GetTourInfoResponse resBoc = (GetTourInfoResponse) msgFactory
				.getResponseMessage(aReqMsg.getID());
		try {
			GetTourInfoRequest rqBoc = (GetTourInfoRequest) aReqMsg;
			int tID = rqBoc.tourID;
			String res = aSession.getTourMgr().getTourDetail(tID);
			resBoc.setSuccess(res);
		} catch (Throwable t) {
			resBoc.setFailure(ResponseCode.FAILURE,
					"Có lỗi xảy ra." + t.getMessage());
			t.printStackTrace();
		} finally {
			aResPkg.addMessage(resBoc);
		}
		return 1;
	}

}
