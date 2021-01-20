package allinone.business;

import java.util.Vector;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.PostEntity;
import allinone.data.ResponseCode;
import allinone.databaseDriven.DatabaseDriver;
import allinone.protocol.messages.GetPostListResponse;

public class GetPostListBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetRichestsBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[GET Wal]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetPostListResponse resPostListResponse = (GetPostListResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try {
            long uid = aSession.getUID();
            mLog.debug("[GET RICHEST]: for" + uid);
            Vector<PostEntity> postLists = DatabaseDriver.getPostList();
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
