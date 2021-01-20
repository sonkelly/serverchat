package allinone.business;

import java.lang.reflect.Field;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.Phong;
import vn.game.room.Zone;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.data.ZoneID;
import allinone.protocol.messages.AnnounceRequest;
import allinone.protocol.messages.AnnounceResponse;

public class AnnounceBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(AnnounceBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) throws ServerException {
        mLog.debug("[Announce]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        AnnounceResponse resAnno = (AnnounceResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try {
            AnnounceRequest rq = (AnnounceRequest) aReqMsg;
            String msg = rq.message;
            resAnno.setSuccess(ResponseCode.SUCCESS, msg);
            Field[] temp = ZoneID.class.getFields();
			for (Field f : temp) {
				int zoneID = (Integer) f.get(new ZoneID());
				if(zoneID != 0){
					Zone zone = aSession.findZone(zoneID);
					Iterable<Phong> phongs = zone.phongValues();
					for(Phong p : phongs){
						p.broadcastZone(resAnno, aSession, false);
					}
				}
			}
        } catch (Throwable t) {
            resAnno.setFailure(ResponseCode.FAILURE, "Lỗi ");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        }
        return 1;
    }
}
