package allinone.business;

import allinone.data.MessagesID;
import allinone.data.SimpleTable;
import allinone.data.UserEntity;
import allinone.databaseDriven.UserDB;

import allinone.protocol.messages.XocDiaOwnerRequest;
import allinone.protocol.messages.XocDiaOwnerResponse;
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

public class XocDiaOwnerBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(XocDiaFireBusiness.class);

    public XocDiaOwnerBusiness() {
    }

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) throws ServerException {
        int result = -2;
        if (aSession.getUserEntity().lockRequest) {
            return result;
        }
        aSession.getUserEntity().lockRequest = true;
        mLog.debug("[XocDiaOwnerBusiness] CATCH");
        MessageFactory msgFactory = aSession.getMessageFactory();
        XocDiaOwnerResponse response = (XocDiaOwnerResponse) msgFactory.getResponseMessage(MessagesID.XOCDIA_OWNER);

        try {
            XocDiaOwnerRequest request = (XocDiaOwnerRequest) aReqMsg;
            long uid = request.uid;
            int isOwner = request.isOwner;
            Room room = aSession.getRoom();

            if (room != null) {
                int zoneID = 0;
                zoneID = aSession.getCurrentZone();
                mLog.debug("[XocDiaOwnerBusiness] CURRENT ZONE ID : " + zoneID + "| room : " + room.getRoomId());
                if (room != null) {
                    mLog.debug("[XocDiaOwnerBusiness] " + room.getName() + "| ");

                }
            }
            //get table money

            UserDB userDb = new UserDB();
            long moneyDB = userDb.getMoney(aSession.getUserEntity().mUid, aSession.getRealMoney());
          
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
