package allinone.business;

import org.slf4j.Logger;

import tools.CacheMatch;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.Room;
import vn.game.session.ISession;
import allinone.data.MatchEntity;
import allinone.data.ResponseCode;
import allinone.data.SimplePlayer;
import allinone.data.SimpleTable;
import allinone.data.UserEntity;
import allinone.data.ZoneID;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.UpdatePlayerCashRequest;
import allinone.protocol.messages.UpdatePlayerCashResponse;

public class UpdatePlayerCashBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(UpdatePlayerCashBusiness.class);

	public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
			IResponsePackage aResPkg) throws ServerException {
		int rtn = PROCESS_FAILURE;		
		MessageFactory msgFactory = aSession.getMessageFactory();
		UpdatePlayerCashResponse res = (UpdatePlayerCashResponse) msgFactory.getResponseMessage(aReqMsg.getID());
		UpdatePlayerCashRequest  rq = (UpdatePlayerCashRequest) aReqMsg;
		res.session = aSession;
		
		mLog.debug("[UPDATE PLAYERCASH] " + rq.matchId + rq.cash);
        
		try {			
			
			MatchEntity matchEntity = CacheMatch.getMatch(rq.matchId);

			if (matchEntity == null) {
				throw new BusinessException(" Bàn chơi đã bị hủy");
			}
			
			int zoneId = matchEntity.getZoneId();
			
			// kiem tra game dang choi co hop le thay doi muc tien khong
			if (zoneId == ZoneID.POKER || zoneId == ZoneID.NXITO || zoneId == ZoneID.LIENG) {
			
				Room room = matchEntity.getRoom();
				
				// thong tin ban choi, kiem tra user co trong playing list khong, update lai tien choi trong ban
				SimpleTable table = room.getAttactmentData();
				
				if(table.isPlaying) {
					throw new BusinessException("Bàn đang chơi, bạn không thay đổi được mức tiền");
				}
				
				long userId = aSession.getUID();
				
				// kiem tra xem co trong ban khong
				SimplePlayer player = table.findPlayer(userId);
				if(player == null) {
					throw new BusinessException("Bạn không ở trong bàn");
				}

				// current user cash
	            UserDB userDb = new UserDB();
	            UserEntity entity = userDb.getUserInfo(userId,aSession.getRealMoney());
	            
	            // real cash
	            if(rq.cash == 0 || entity.money == 0) {
	            	throw new BusinessException("Bạn không đủ tiền để tham gia chơi");  
	            }
	            
	            mLog.debug(" Current Money and play cash " + entity.money + " " + rq.cash);
	            
	            if (rq.cash > entity.money) {
	            	rq.cash = entity.money;
	            }
	            
	            // validate money take
	      	
	            player.cash = rq.cash;
	            
	            res.setSuccess(ResponseCode.SUCCESS, player.id, player.cash);
	            
	            // broadcast message
	            table.broadcastMsg(res, table.getNewPlayings(), table.getNewWaitings(), player, true);
	            
	            return PROCESS_OK;
				
			} else {
				throw new BusinessException(" Game chơi không hợp lệ");
			}
			
		} catch (Throwable t) {
			res.setFailure(ResponseCode.FAILURE, t.getMessage());
			aResPkg.addMessage(res);
			rtn = PROCESS_OK;		
		} 
		
		return rtn;
	}
}
