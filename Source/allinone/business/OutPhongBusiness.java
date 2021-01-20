package allinone.business;


import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.room.Phong;
import vn.game.room.Zone;
import vn.game.session.ISession;

public class OutPhongBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(OutPhongBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        try {
            int zoneID = aSession.getCurrentZone();
            Zone zone = aSession.findZone(zoneID);
            mLog.debug("Phong:" + aSession.getPhongID());
            if(aSession.getPhongID() != 0){
                Phong currentPhong = zone.getPhong(aSession.getPhongID());
                if(currentPhong == null)
                {
                    mLog.warn("OutPhongBusiness currentPHong= null [PhongID]" + aSession.getPhongID());
                }
                currentPhong.outPhong(aSession);
            }
            
        } catch (Throwable t) {
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        }
        return 1;
    }
}
