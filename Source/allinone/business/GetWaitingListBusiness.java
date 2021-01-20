package allinone.business;

import java.util.Vector;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.RoomEntity;
import vn.game.room.Zone;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.data.ZoneID;
import allinone.protocol.messages.GetWaitingListRequest;
import allinone.protocol.messages.GetWaitingListResponse;

public class GetWaitingListBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(GetWaitingListBusiness.class);

	public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
			IResponsePackage aResPkg) {
		mLog.debug("[GET WAITING ROOM LIST]: Catch : "
				+ aSession.getCurrentZone());
                
                aSession.getCollectInfo().append("->GetWaitingRoomList: ");
                
		MessageFactory msgFactory = aSession.getMessageFactory();
		GetWaitingListResponse resGetWaitingList = (GetWaitingListResponse) msgFactory
				.getResponseMessage(aReqMsg.getID());
		try {
			GetWaitingListRequest rqGetWaitingList = (GetWaitingListRequest) aReqMsg;
			Zone zone = aSession.findZone(aSession.getCurrentZone());

			if (zone == null) {
				mLog.error("Error : Cant get zone ! "
						+ aSession.getCurrentZone() + " : ["
						+ aSession.getUserName() + "]");
				if (aSession.getCurrentZone() == -1) {
					aSession.setCurrentZone(ZoneID.BACAY);
//					DatabaseDriver.updateUserZone(aSession.getUID(),
//							ZoneID.BACAY);
					zone = aSession.findZone(aSession.getCurrentZone());
				}
			}
			Vector<RoomEntity> waitingRooms = zone.dumpWaitingRooms(
					rqGetWaitingList.mOffset, rqGetWaitingList.mLength,
					rqGetWaitingList.level, rqGetWaitingList.minLevel,
					aSession.getCurrentZone());
			mLog.debug("[GET WAITING ROOM LIST]: size - " + waitingRooms.size());
			resGetWaitingList.setSuccess(ResponseCode.SUCCESS,
					waitingRooms.size(), waitingRooms,
					aSession.getCurrentZone());

		} catch (Throwable t) {
			resGetWaitingList.setFailure(ResponseCode.FAILURE,
					"Không thể kết nối đến cơ sở dữ liệu ");
			mLog.error("Process message " + aReqMsg.getID() + " error.", t);
		} finally {
			if ((resGetWaitingList != null)) {
				aResPkg.addMessage(resGetWaitingList);
			}
		}

		return 1;
	}
}
