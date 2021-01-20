package allinone.business;

import java.util.Vector;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.MessageOfflineEntity;
import allinone.data.ResponseCode;
import allinone.databaseDriven.DatabaseDriver;
import allinone.protocol.messages.OfflineMessageResponse;

/**
 *
 * @author Dinhpv
 */
public class OfflineMessageBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(OfflineMessageBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) throws ServerException {
        mLog.debug("[GET Wal]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        OfflineMessageResponse resPostListResponse = (OfflineMessageResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try {
            long uid = aSession.getUID();
            mLog.debug("[GET OFFLINE MESSAGE]: for" + uid);
            Vector<MessageOfflineEntity> postLists = DatabaseDriver.getMessageOffline(uid);
            resPostListResponse.setSuccess(ResponseCode.SUCCESS, postLists);
        } catch (Throwable t) {
            resPostListResponse.setFailure(ResponseCode.FAILURE, "Không thể kết nối đến cơ sở dữ liệu ");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally {
            if ((resPostListResponse != null)) {
                aResPkg.addMessage(resPostListResponse);
            }
        }
        return 1;
    }
}
