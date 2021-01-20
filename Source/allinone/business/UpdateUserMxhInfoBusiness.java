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
import allinone.data.UserEntity;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.UpdateUserMxhInfoRequest;
import allinone.protocol.messages.UpdateUserMxhInfoResponse;

/**
 * 
 * @author Dinhpv
 */
public class UpdateUserMxhInfoBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(UpdateUserMxhInfoBusiness.class);

	public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
			IResponsePackage aResPkg) throws ServerException {

		int rtn = PROCESS_FAILURE;
		mLog.debug("[UPDATE USER] : Catch");
		UserDB userDb = new UserDB();

		MessageFactory msgFactory = aSession.getMessageFactory();

		UpdateUserMxhInfoResponse resUpdate = (UpdateUserMxhInfoResponse) msgFactory
				.getResponseMessage(aReqMsg.getID());
		try {
			UpdateUserMxhInfoRequest rqRegister = (UpdateUserMxhInfoRequest) aReqMsg;
                        userDb.updateUserMxhInfo(aSession.getUID(), rqRegister.cityId, rqRegister.address,
                                rqRegister.jobId, rqRegister.birthday, rqRegister.hobby, rqRegister.nickSkype,
                                rqRegister.nickYahoo, rqRegister.phoneNumber, rqRegister.sex, rqRegister.avatarFileId,
                                rqRegister.status, rqRegister.characterId);
                        
                        resUpdate.mCode = ResponseCode.SUCCESS;
                        

			rtn = PROCESS_OK;
                        UserEntity entity = new UserEntity();
                        entity.mUid = aSession.getUID();
                        CacheUserInfo userInfo = new CacheUserInfo();
                        userInfo.deleteFullCacheUser(entity);
                                
		} catch (Throwable t) {
			resUpdate.setFailure(ResponseCode.FAILURE,
					"Dữ liệu bạn nhập không chính xác!");
			rtn = PROCESS_OK;
			mLog.error("Process message " + aReqMsg.getID() + " error.", t);
		} finally {
			if ((resUpdate != null) && (rtn == PROCESS_OK)) {
				aResPkg.addMessage(resUpdate);
			}
		}
		return rtn;
	}
}
