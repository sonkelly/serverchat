package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.databaseDriven.BlogDB;
import allinone.protocol.messages.UpdateBlogRequest;
import allinone.protocol.messages.UpdateBlogResponse;

public class UpdateBlogBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(UpdateBlogBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[set social avatar] : Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        UpdateBlogResponse resSocialAvar =
                (UpdateBlogResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try {
            UpdateBlogRequest rqSocialAvar = (UpdateBlogRequest) aReqMsg;
            BlogDB db = new BlogDB();
            db.updateBlog(rqSocialAvar.blogId,  rqSocialAvar.title, rqSocialAvar.subject, rqSocialAvar.post, 0);
            resSocialAvar.mCode = ResponseCode.SUCCESS;
            
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
