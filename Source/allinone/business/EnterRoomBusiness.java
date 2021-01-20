package allinone.business;

import java.util.List;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.Phong;
import vn.game.room.Zone;
import vn.game.room.ZoneManager;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.data.SimpleTable;
import allinone.protocol.messages.EnterRoomRequest;
import allinone.protocol.messages.EnterRoomResponse;
import allinone.server.Server;

public class EnterRoomBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(EnterRoomBusiness.class);
@Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        MessageFactory msgFactory = aSession.getMessageFactory();
        EnterRoomResponse resEnter = (EnterRoomResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        resEnter.session = aSession;

        mLog.debug("[ENTER ROOM]: Catch");
        String zoneName = "#";
        try {
            EnterRoomRequest rqEnter = (EnterRoomRequest) aReqMsg;
            int roomID;

            int zoneID = aSession.getCurrentZone();
            resEnter.setZoneID(zoneID);
            Zone zone = aSession.findZone(zoneID);
            Phong enterPhong;

            if (zone == null) {
                //fix error reconnect for client
                ZoneManager zManager = Server.getWorker().getZoneManager();
                enterPhong = zManager.findPhong(rqEnter.roomID);
            } else {
                //Set Phong
                if (aSession.getPhongID() != 0) {
                    enterPhong = zone.getPhong(aSession.getPhongID());
                    if (enterPhong == null) {
                    } else {
                        if (rqEnter.roomID == 0) {
                            rqEnter.roomID = enterPhong.id;
                        } else {
                            enterPhong.outPhong(aSession);
                        }
                    }
                }
                enterPhong = zone.getPhong(rqEnter.roomID);
            }
            if (enterPhong == null) {
                mLog.warn("EnterRoomBussiness [RoomID]" + rqEnter.roomID);
                throw new BusinessException("Khong vao duoc game nay");
            }
            
            enterPhong.enterPhong(aSession);

            roomID = enterPhong.id;
            
            List<SimpleTable> tables = zone.dumpNewWaitingTables(roomID);
            resEnter.setSuccess(ResponseCode.SUCCESS, tables);

            aResPkg.addMessage(resEnter);

        } catch (Throwable t) {
            resEnter.setFailure(ResponseCode.FAILURE, "Hiện tại không vào được game " + zoneName + " này!");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
            aResPkg.addMessage(resEnter);
        }
        return 1;
    }
}
