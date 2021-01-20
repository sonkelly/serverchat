package allinone.business;

import java.util.List;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.data.SimpleTable;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.TransferCashRequest;
import allinone.protocol.messages.TransferCashResponse;

public class TransferCashBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(TransferCashBusiness.class);

	public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
			IResponsePackage aResPkg) {
		// process's status
		int rtn = PROCESS_FAILURE;
		mLog.debug("[TRANSFER_CASH]: Catch");
		//
		MessageFactory msgFactory = aSession.getMessageFactory();
		TransferCashResponse resTransferCash = (TransferCashResponse) msgFactory
				.getResponseMessage(aReqMsg.getID());
                resTransferCash.session = aSession;
		try {
//                    if(true)
//                        throw new BusinessException("Tính năng này tạm thời ngừng hoạt động");
                    
			TransferCashRequest rqBuyAvatar = (TransferCashRequest) aReqMsg;

			long s_uid = aSession.getUID();
                        List<ISession> lstSessions = aSession.getManager().findAllSession(s_uid);
                        if(lstSessions.size()>1)
                        {
                            throw new BusinessException("Bạn không thể chuyển tiền vì đăng nhập bằng 2 nick"); 
                        }
                        
			long d_uid;
			String d_name = rqBuyAvatar.destName;
			long money = rqBuyAvatar.money;
			// long currMoney = DatabaseDriver.getUserMoney(s_uid);

			//
			// if (currMoney >= money) {
			// if (currMoney - money < 50000) {
			// resTransferCash.setFailure(ResponseCode.FAILURE,
			// "Số tiền bạn chuyển vượt quá theo quy định so với số tiền bạn có.");
			// } else {
//                        ISession ses = aSession.getManager().findSession(d_uid);
                        if (aSession.getRoom() != null) 
                        {
                            SimpleTable table = aSession.getRoom().getAttactmentData();
				if (table != null && table.isPlaying) {
					throw new BusinessException("Bạn không được chuyển tiền khi bàn đang chơi");
				}
                        }
                        
			UserDB userDb = new UserDB();
			d_uid = userDb.transferMoney(s_uid, d_name, money);

			if (d_uid == -1) {
				resTransferCash.setFailure(ResponseCode.FAILURE,
						"Tài khoản đích không tồn tại");

			} else if (d_uid == -2) {
				resTransferCash.setFailure(ResponseCode.FAILURE,
						"Bạn không đủ tiền để chuyển.");

			} else if (d_uid == -3) {
				resTransferCash.setFailure(ResponseCode.FAILURE,
						"Số tiền còn lại của bản phải lớn hơn 3000 Bi.");

			} else
                        {
				resTransferCash.setSuccess(ResponseCode.SUCCESS, s_uid, d_uid,
						money, true);
				resTransferCash.src_name = aSession.getUserName();
//				ISession ses = aSession.getManager().findSession(d_uid);
//				if (ses != null) {
//					/*Zone zone = ses.findZone(ses.getCurrentZone());
//					if (zone != null) {
//						Room room = zone.findRoom(ses.getRoomID());
//						if (room != null) {
//							room.broadcastMessage(resTransferCash, ses, true);
//						}
//					} else {*/
//						ses.write(resTransferCash);
//					//}
//
//				}
			}
			// }
			//
			// } else {
			// resTransferCash.setFailure(ResponseCode.FAILURE,
			// "Bạn không đủ tiền để chuyển.");
			// }

			rtn = PROCESS_OK;
		} 
                catch(BusinessException be)
                {
                    resTransferCash.setFailure(ResponseCode.FAILURE, be.getMessage());
                    rtn = PROCESS_OK;

                }
                catch (Throwable t) {
			resTransferCash.setFailure(ResponseCode.FAILURE, "Bị lỗi: ");
			mLog.debug("Bị lỗi: " + t.getMessage());
			rtn = PROCESS_OK;
		} finally {
			if ((resTransferCash != null) && (rtn == PROCESS_OK)) {
				aResPkg.addMessage(resTransferCash);
			}
		}
		return rtn;
	}
}
