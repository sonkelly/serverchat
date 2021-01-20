package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.databaseDriven.DatabaseDriver;
import allinone.protocol.messages.GetImageRequest;
import allinone.protocol.messages.GetImageResponse;

public class GetImageBusiness  extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(GetImageBusiness.class);

	public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
			IResponsePackage aResPkg) {
		int rtn = PROCESS_FAILURE;
		MessageFactory msgFactory = aSession.getMessageFactory();
		GetImageResponse resGetI = (GetImageResponse) msgFactory
				.getResponseMessage(aReqMsg.getID());
                
                resGetI.session = aSession;
		mLog.debug("[GetImage]: Catch");
		try {
			// request message and its values
			GetImageRequest rqGetI = (GetImageRequest) aReqMsg;
			String name = rqGetI.name;
			String image = DatabaseDriver.getImage(name);
			resGetI.setSuccess(ResponseCode.SUCCESS, image, name);
			aSession.write(resGetI);
			rtn = PROCESS_OK;
		} catch (Throwable t) {
			// response failure
			resGetI.setFailure(ResponseCode.FAILURE, "Không tìm thấy ảnh!");
			//aSession.setLoggedIn(false);
			rtn = PROCESS_OK;
			mLog.error("Process message " + aReqMsg.getID() + " error.", t);
			aResPkg.addMessage(resGetI);
		}

		return rtn;
	}

}
