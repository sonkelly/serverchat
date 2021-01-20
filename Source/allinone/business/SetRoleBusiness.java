package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.databaseDriven.InfoDB;
import allinone.protocol.messages.SetRoleRequest;
import allinone.protocol.messages.SetRoleResponse;

public class SetRoleBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(SetRoleBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[set social avatar] : Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        SetRoleResponse resSocialAvar =
                (SetRoleResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try {
            SetRoleRequest rqSocialAvar = (SetRoleRequest) aReqMsg;
            InfoDB db = new InfoDB();
            db.setRole(rqSocialAvar.systemObjectId, rqSocialAvar.roleId, rqSocialAvar.systemObjectRecordId);
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
