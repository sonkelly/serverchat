/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package allinone.business;

import org.slf4j.Logger;

import tools.CacheUserInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.UpdateUserInfoRequest;
import allinone.protocol.messages.UpdateUserInfoResponse;

public class UpdateUserInfoBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(UpdateUserInfoBusiness.class);

	public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
			IResponsePackage aResPkg) throws ServerException {
		int rtn = PROCESS_FAILURE;		
		mLog.debug("[UPDATE USER] : Catch");
		UserDB userDb = new UserDB();

		MessageFactory msgFactory = aSession.getMessageFactory();
		UpdateUserInfoResponse res = (UpdateUserInfoResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        res.session = aSession;
        
		try {			
			UpdateUserInfoRequest rq = (UpdateUserInfoRequest) aReqMsg;
			res.setFailure(ResponseCode.FAILURE, "Tính năng chưa được mở");
//			if(rq.name == null || "".equals(rq.name)) {
//				res.setFailure(ResponseCode.FAILURE, "Vui lòng nhập đủ dữ liệu");
//			} else if(rq.cmt == null || "".equals(rq.cmt)) {
//				res.setFailure(ResponseCode.FAILURE, "Vui lòng nhập đủ dữ liệu");
//			} else if(rq.address == null || "".equals(rq.address)) {
//				res.setFailure(ResponseCode.FAILURE, "Vui lòng nhập đủ dữ liệu");
//			} else {				
//				userDb.updateUserInfo(aSession.getUID(), rq.name, rq.cmt, rq.address);				
//				CacheUserInfo cache = new CacheUserInfo();
//                cache.deleteCacheUser(aSession.getUserEntity());                
//				res.setSuccess(ResponseCode.SUCCESS, "Cập nhật thành công");
//			}

			rtn = PROCESS_OK;
			
		} catch (Throwable t) {
			res.setFailure(ResponseCode.FAILURE,"Dữ liệu bạn nhập không chính xác!");
			rtn = PROCESS_OK;
		
		} finally {
			aResPkg.addMessage(res);
		}
		
		return rtn;
	}
}
