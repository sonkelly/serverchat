package allinone.business;

import allinone.data.MessagesID;
import allinone.data.SimpleTable;
import allinone.data.UserEntity;
import allinone.databaseDriven.UserDB;

import allinone.protocol.messages.XocDiaBanRequest;
import allinone.protocol.messages.XocDiaBanResponse;
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

public class XocDiaBanBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(XocDiaFireBusiness.class);

    public XocDiaBanBusiness() {
    }

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) throws ServerException {
        int result = -2;
        if (aSession.getUserEntity().lockRequest) {
            return result;
        }
        aSession.getUserEntity().lockRequest = true;
        mLog.debug("[XOCDIA_BANCUA] CATCH");
        MessageFactory msgFactory = aSession.getMessageFactory();
        XocDiaBanResponse response = (XocDiaBanResponse) msgFactory.getResponseMessage(MessagesID.XOCDIA_BANCUA);

        return result;
    }
}
