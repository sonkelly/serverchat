package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.Room;
import vn.game.room.Zone;
import vn.game.session.ISession;
import allinone.data.SimpleTable;
import allinone.data.ZoneID;
import allinone.protocol.messages.SessionActionRequest;
import allinone.protocol.messages.SessionActionResponse;
import java.util.Calendar;

public class SessionActionBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(SessionActionBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) {

        int rtn = PROCESS_FAILURE;

        MessageFactory msgFactory = aSession.getMessageFactory();
        SessionActionResponse resChange = (SessionActionResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        aSession.setLastAccessTime(Calendar.getInstance().getTime());

        resChange.session = aSession;

        try {

            SessionActionRequest rqChange = (SessionActionRequest) aReqMsg;
            int zoneID = aSession.getCurrentZone();
            Zone chatZone = aSession.findZone(zoneID);
            Room room = chatZone.findRoom(rqChange.roomID);

            if (room != null) {

                SimpleTable table = (SimpleTable) room.getAttactmentData();

                mLog.debug(" money table phong id phong id " + rqChange.roomID + "  " + table.getPhongID() + " " + room.getRoomId() + " " + room.getPhongID());
                mLog.debug("aSession hide:"+aSession.isHidden());
                if (table.isPlaying) {

                    switch (zoneID) {

                        case ZoneID.BINH: {
                            if (rqChange.status == 1) {
                                aSession.setHide(true);
                            } else if (rqChange.status == 2) {
                                aSession.setHide(false);
                            }

                            break;
                        }

                        default:
                            break;
                    }

                    
                }
                mLog.debug("aSession hide:"+aSession.isHidden());
            } else {

            }

            rtn = PROCESS_OK;
        } catch (Exception t) {

        }
        return rtn;
    }

}
