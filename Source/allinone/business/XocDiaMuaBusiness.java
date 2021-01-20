package allinone.business;

import allinone.data.MessagesID;
import allinone.data.SimpleTable;
import allinone.data.UserEntity;
import allinone.data.ZoneID;
import allinone.databaseDriven.UserDB;

import allinone.protocol.messages.XocDiaMuaRequest;
import allinone.protocol.messages.XocDiaMuaResponse;
import java.io.PrintStream;
import org.slf4j.Logger;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.Room;
import vn.game.session.ISession;
import vn.game.workflow.SimpleWorkflow;

public class XocDiaMuaBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(XocDiaFireBusiness.class);

    public XocDiaMuaBusiness() {
    }

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) throws ServerException {
        int result = -2;
        if (aSession.getUserEntity().lockRequest) {
            return result;
        }
        aSession.getUserEntity().lockRequest = true;
        mLog.debug("[XocDiaMuaBusiness] CATCH");
        MessageFactory msgFactory = aSession.getMessageFactory();
        XocDiaMuaResponse response = (XocDiaMuaResponse) msgFactory.getResponseMessage(MessagesID.XOCDIA_MUACUA);

        try {
            XocDiaMuaRequest request = (XocDiaMuaRequest) aReqMsg;

            Room room = aSession.getRoom();

            if (room != null) {
                int zoneID = 0;
                zoneID = aSession.getCurrentZone();
                mLog.debug("[XocDiaMuaBusiness] CURRENT ZONE ID : " + zoneID + "| room : " + room.getRoomId());
                if (room != null) {
                    mLog.debug("[XocDiaMuaBusiness] " + room.getName() + "| ");

                }
            }
            //get table money

       
        } catch (Throwable t) {
            response.setFailure("Có lỗi xảy ra, vui lòng thử lại");
            aSession.getUserEntity().lockRequest = false;
            aResPkg.addMessage(response);
        } finally {
            aSession.getUserEntity().lockRequest = false;
            aResPkg.addMessage(response);
        }
        return result;
    }
}
