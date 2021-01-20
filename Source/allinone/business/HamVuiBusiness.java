/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import java.util.List;

import org.json.JSONException;
import org.slf4j.Logger;

import tools.CacheMatch;
import tools.CacheUserInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.Phong;
import vn.game.room.Room;
import vn.game.room.Zone;
import vn.game.session.ISession;
import allinone.data.MatchEntity;
import allinone.data.Messages;
import allinone.data.MessagesID;
import allinone.data.ResponseCode;
import allinone.data.SimpleException;
import allinone.data.SimplePlayer;
import allinone.data.SimpleTable;
import allinone.data.UserEntity;
import allinone.data.ZoneID;
import allinone.databaseDriven.RoomDB;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.EnterRoomResponse;
import allinone.protocol.messages.EnterZoneResponse;
import allinone.protocol.messages.HamVuiRequest;
import allinone.protocol.messages.HamVuiResponse;

/**
 * 
 * @author binh_lethanh
 */
public class HamVuiBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(HamVuiBusiness.class);

	// private static final int TIMES = 4;
	// private static final String MSG_NOT_ENOUGH_MONEY =
	// "Bạn không đủ tiền để tham gia. Số tiền bạn có nhỏ hơn 4 lần tiền bàn";
	// private static final String MSG_FULL_TABLE =
	// "Bàn này đầy rồi, bạn thông cảm chờ nhé!";
	 public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
		mLog.debug("[Reconnect ROOM]: Catch");
		MessageFactory msgFactory = aSession.getMessageFactory();
		HamVuiResponse resReconn = (HamVuiResponse) msgFactory
				.getResponseMessage(aReqMsg.getID());
		aSession.setByteProtocol(2);
		try {
			HamVuiRequest rqReconn = (HamVuiRequest) aReqMsg;
			UserEntity newUser;
			UserDB db = new UserDB();

			switch (rqReconn.type) {
			case 1: { // home
				resReconn.setSuccess();
				resReconn.isNeeded = false;
				newUser = db.getUserInfo(rqReconn.username,aSession.getRealMoney());
				aSession.setUID(newUser.mUid);
				aSession.setUserName(newUser.mUsername);
				aSession.setLoggedIn(true);
				aSession.write(resReconn);

				return 1;
			}
			case 2: { // Vao Game
				newUser = db.getUserInfo(rqReconn.username,aSession.getRealMoney());
				aSession.setUID(newUser.mUid);
				aSession.setUserName(newUser.mUsername);
				aSession.setLoggedIn(true);
				enterZone(aSession, rqReconn.zone, resReconn);
				return 1;
			}
			case 3: { // Vao Phong
				newUser = db.getUserInfo(rqReconn.username,aSession.getRealMoney());
				aSession.setUID(newUser.mUid);
				aSession.setUserName(newUser.mUsername);
				aSession.setLoggedIn(true);
				enterRoom(aSession, rqReconn.phong, rqReconn.zone, resReconn);
				return 1;
			}
			case 4: {// match
				MatchEntity matchEntity = CacheMatch.getMatch(rqReconn.matchId);
				if (matchEntity == null || matchEntity.getRoom() == null) {
					newUser = db.getUserInfo(rqReconn.username,aSession.getRealMoney());
					aSession.setUID(newUser.mUid);
					aSession.setUserName(newUser.mUsername);
					aSession.setLoggedIn(true);
					enterRoom(aSession, rqReconn.phong, rqReconn.zone, resReconn);
					return 1;
				}

				Room room = null;
				int zoneID = 0;

				long uid = rqReconn.uid;
				room = matchEntity.getRoom();
				room.join(aSession);
				zoneID = matchEntity.getZoneId();
				aSession.setCurrentZone(zoneID);
				Phong enterPhong = aSession.findZone(zoneID).getPhong(
						matchEntity.getPhongID());
				if (enterPhong != null) {
					enterPhong.enterPhong(aSession);
				}
                                CacheUserInfo cacheUser = new CacheUserInfo();
				newUser = cacheUser.getUserInfo(uid,aSession.getRealMoney());
				if (newUser == null) {
					throw new BusinessException(Messages.NONE_EXISTS_PLAYER);
				}
				SimpleTable tb = (SimpleTable) room.getAttactmentData();
				if (!tb.isPlaying) {
					resReconn.setFailure(ResponseCode.FAILURE,
							"Ván chơi đã kết thúc");
					aSession.setUID(newUser.mUid);
					aSession.setUserName(newUser.mUsername);
					aSession.setLoggedIn(true);
					return 1;
				}
				boolean isNewMatch = true;
				for (SimplePlayer p : tb.getNewPlayings()) {
					if (p.id == newUser.mUid) {
						isNewMatch = false;
						break;
					}

				}
				if (isNewMatch) {
					resReconn.setFailure(ResponseCode.FAILURE,
							"Ván chơi đã kết thúc và bắt đầu ván mới.");
					aSession.write(resReconn);
					aSession.setUID(newUser.mUid);
					aSession.setUserName(newUser.mUsername);
					aSession.setLoggedIn(true);
					return 1;
				}
				switch (zoneID) {
				default: {
					resReconn.setFailure(ResponseCode.FAILURE, "Ván chơi đang diễn ra.");
					aSession.write(resReconn);
					break;
				}
				}

				aSession.setUID(newUser.mUid);
				aSession.setUserName(newUser.mUsername);
				aSession.setLoggedIn(true);
				return 1;
			}
			default:
				break;
			}

		} catch (BusinessException ex) {
			aSession.setLoggedIn(false);
			aSession.setCommit(false);
			resReconn.setFailure(ResponseCode.FAILURE, ex.getMessage());
			aResPkg.addMessage(resReconn);
			mLog.debug(ex.getMessage());
		} catch (Throwable t) {
			aSession.setLoggedIn(false);
			aSession.setCommit(false);
			mLog.error("Process message " + aReqMsg.getID() + " error.", t);
			resReconn.setFailure(ResponseCode.FAILURE, "Lỗi xảy ra");
			aResPkg.addMessage(resReconn);
		}
		return 1;
	}

	private void enterRoom(ISession aSession, int roomID, int zoneID, HamVuiResponse res) {
		MessageFactory msgFactory = aSession.getMessageFactory();
		EnterRoomResponse resEnter = (EnterRoomResponse) msgFactory
				.getResponseMessage(MessagesID.EnterRoom);
		try {
			aSession.setCurrentZone(zoneID);
			resEnter.setZoneID(zoneID);
			Zone zone = aSession.findZone(zoneID);
			Phong enterPhong;

			if (aSession.getPhongID() != 0) {
				enterPhong = zone.getPhong(aSession.getPhongID());
				if (enterPhong == null) {
				} else {
					enterPhong.outPhong(aSession);
				}
			}
			enterPhong = zone.getPhong(roomID);
			if (enterPhong == null) {
				mLog.warn("EnterRoomBussiness [RoomID]" + roomID);
			} else {
				enterPhong.enterPhong(aSession);
			}
			resEnter.session = aSession;
			List<SimpleTable> tables = zone.dumpNewWaitingTables(roomID);
			resEnter.setSuccess(ResponseCode.SUCCESS, tables);

			aSession.write(resEnter);
		} catch (Throwable e) {
			mLog.debug("Error!");
			res.setFailure(ResponseCode.FAILURE, e.getMessage());
			try {
				aSession.write(res);
			}catch (Throwable e1) {
			}
		}
	}

	private void enterZone(ISession aSession, int zoneID, HamVuiResponse res) {
		try {
			MessageFactory msgFactory = aSession.getMessageFactory();
			EnterZoneResponse resEnter = (EnterZoneResponse) msgFactory
					.getResponseMessage(MessagesID.ENTER_ZONE);
			aSession.setCurrentZone(zoneID);
			resEnter.timeout = ZoneID.getTimeout(zoneID);
			resEnter.session = aSession;
			RoomDB roomDB = new RoomDB();
			resEnter.lstRooms = roomDB.getRooms(zoneID,aSession.getRealMoney());
			resEnter.setSuccess(ResponseCode.SUCCESS);
			aSession.setPhongID(0);
			aSession.write(resEnter);
		} catch (Throwable e) {
			mLog.debug("Error!");
			res.setFailure(ResponseCode.FAILURE, e.getMessage());
			try {
				aSession.write(res);
			}catch (Throwable e1) {
			}
		}
	}

}
