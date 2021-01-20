package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.MessagesID;
import allinone.data.ResponseCode;
import allinone.data.ZoneID;
import allinone.databaseDriven.RoomDB;
import allinone.protocol.messages.PlayEnterZoneRequest;
import allinone.protocol.messages.PlayEnterZoneResponse;
import allinone.protocol.messages.UpdateCashRequest;
import java.util.Calendar;
import vn.game.common.SessionUtils;

public class PlayEnterZoneBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(PlayEnterZoneBusiness.class);

//    private void correctWrongTable(ISession aSession, MessageFactory msgFactory) {
//        try {
//            if (aSession.getRoom() != null && aSession.getRoom().getAttactmentData() != null) {
//
//                IResponsePackage responsePkg = aSession.getDirectMessages();
//                IBusiness business = msgFactory.getBusiness(MessagesID.MATCH_CANCEL);
//
//                CancelRequest cancelRq = (CancelRequest) msgFactory.getRequestMessage(MessagesID.MATCH_CANCEL);
//                cancelRq.mMatchId = aSession.getRoom().getRoomId();
//                cancelRq.isSendMe = false;
//                business.handleMessage(aSession, cancelRq, responsePkg);
//
//            }
//        } catch (Throwable ex) {
//            if (ex != null) {
//                mLog.error(ex.getMessage(), ex);
//            }
//        }
//    }
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        MessageFactory msgFactory = aSession.getMessageFactory();
        PlayEnterZoneResponse resEnter = (PlayEnterZoneResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        //mLog.debug("[ENTER ZONE]: Catch");
        String zoneName = "#";
        
        if (!SessionUtils.checkSessionDB(aSession)) {
            return 0;
        }
       
        try {

            PlayEnterZoneRequest rqEnter = (PlayEnterZoneRequest) aReqMsg;
            int zoneID = rqEnter.zoneID;

            //mLog.debug(" Enter ZOne id " + zoneID);
            zoneName = ZoneID.getZoneName(zoneID);

            // validate if user in room then cancel request
//            long uid = aSession.getUID();            
//            Room room = aSession.getRoom();
//            if (room != null) {
//            	mLog.debug(" Out khoi game tu dong ");
//                long matchID = room.getRoomId();
//                if (matchID > 1) {
//                	  // Case
//                	IBusiness business = msgFactory.getBusiness(MessagesID.MATCH_CANCEL);
//                    CancelRequest rqMatchCancel = (CancelRequest) msgFactory.getRequestMessage(MessagesID.MATCH_CANCEL);
//                    rqMatchCancel.uid = uid;
//                    rqMatchCancel.mMatchId = matchID;
//                    rqMatchCancel.isLogout = false;
//                    rqMatchCancel.isSendMe = false;
//                    try {
//                        business.handleMessage(aSession, rqMatchCancel, aSession.getDirectMessages());
//                    } catch (ServerException se) {
//                		mLog.error("cancelTable " +  se.getCause());
//                    } catch (Throwable e) {
//                		mLog.error("cancelTable " +  e.getCause());
//                    }
//                }
//            }
            //mLog.debug("Zone name = " + zoneName);
            aSession.setCurrentZone(zoneID);
            aSession.setLastAccessTime(Calendar.getInstance().getTime());
            resEnter.timeout = ZoneID.getTimeout(zoneID);
            resEnter.session = aSession;
            aSession.setCurrPosition(null);

            RoomDB db = new RoomDB();
            //mLog.debug("money:"+aSession.getRealMoney());
            resEnter.lstRooms = db.getRooms(zoneID,aSession.getRealMoney());

            resEnter.setSuccess(ResponseCode.SUCCESS);

            aSession.setPhongID(0);

            aResPkg.addMessage(resEnter);

            // gui message 120002 update money
            updateMoney(aSession, aResPkg);

            // sent message test
//            SendMessageRequest ms = (SendMessageRequest) msgFactory.getRequestMessage(MessagesID.SendMess);
//            IResponsePackage responsePkg = aSession.getDirectMessages();
//            IBusiness business = msgFactory.getBusiness(MessagesID.SendMess);
//            try {
//				business.handleMessage(aSession, ms, responsePkg);
//			} catch (ServerException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//send list phong
            ListPhongBusiness lsB = new ListPhongBusiness();
            lsB.sendListPhong(aSession);
            //end
        } catch (Throwable t) {
            resEnter.setFailure(ResponseCode.FAILURE, "Hiện tại không vào được game " + zoneName + " này!");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
            aResPkg.addMessage(resEnter);
        }
        return 1;
    }

    private void updateMoney(ISession aSession, IResponsePackage aResPkg) {
        MessageFactory msgFactory = aSession.getMessageFactory();
        IBusiness business = msgFactory.getBusiness(MessagesID.UPDATEMONEY);
        UpdateCashRequest request = (UpdateCashRequest) msgFactory.getRequestMessage(MessagesID.UPDATEMONEY);
        try {
            business.handleMessage(aSession, request, aResPkg);
        } catch (ServerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
