/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import org.slf4j.Logger;

import tools.CacheMatch;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.room.Room;
import vn.game.room.Zone;
import vn.game.session.ISession;
import allinone.data.MatchEntity;
import allinone.protocol.messages.BeatMagicRequest;

/**
 *
 * @author Vostro 3450
 */
public class BeatMagicBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(BeatMagicBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) {

        mLog.debug("[BeatMagic]: Catch");
        try {
            BeatMagicRequest rqAn = (BeatMagicRequest) aReqMsg;
            long uid = aSession.getUID();
            long matchID = rqAn.matchID;
            MatchEntity matchEntity = CacheMatch.getMatch(rqAn.matchID);
            Room room = null;
            if (matchEntity != null) {
                room = matchEntity.getRoom();
            }
            if (room == null || (room.getAttactmentData().matchID != matchID)) {
                Zone zone = aSession.findZone(aSession.getCurrentZone());
                room = zone.findRoom(matchID);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return 1;
    }
}
