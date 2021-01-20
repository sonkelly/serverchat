package allinone.business;

import org.slf4j.Logger;

import tools.CacheWallInfo;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.databaseDriven.WallDB;
import allinone.protocol.messages.PostWallRequest;
import allinone.protocol.messages.PostWallResponse;


public class PostWallBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(PostWallBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[Post wall]");
        MessageFactory msgFactory = aSession.getMessageFactory();
        PostWallResponse resSocialAvar =
                (PostWallResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try {
            PostWallRequest rqSocialAvar = (PostWallRequest) aReqMsg;
            WallDB db = new WallDB();
            db.postWall(aSession.getUID(), rqSocialAvar.destId, rqSocialAvar.mind, rqSocialAvar.fileId);
            
            resSocialAvar.mCode = ResponseCode.SUCCESS;
            CacheWallInfo cache = new CacheWallInfo();
            cache.deleteCache(rqSocialAvar.destId);
            
            
        } catch (Exception e) {
            resSocialAvar.setFailure(ResponseCode.FAILURE, "Không thể kết nối đến cư sở dữ liệu");
            mLog.error(e.getMessage(), e);
            
        } finally {
            if ((resSocialAvar != null)) {
                aResPkg.addMessage(resSocialAvar);
            }
        }
        return 1;
    }
}
