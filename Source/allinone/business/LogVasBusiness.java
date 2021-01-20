package allinone.business;

import allinone.data.AIOConstants;
import java.util.ArrayList;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.LogvascEntity;
import allinone.data.ResponseCode;
import allinone.protocol.messages.LogVasRequest;
import allinone.protocol.messages.LogVasResponse;
import allinone.databaseDriven.LogvascDB;

public class LogVasBusiness extends AbstractBusiness {

    private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(LogVasBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) {

        mLog.debug("[Get logsVas]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        LogVasResponse response = (LogVasResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        LogVasRequest req = (LogVasRequest) aReqMsg;
        try {

            long uid = aSession.getUID();

            LogvascDB LogVasObj = new LogvascDB();
            int page = req.page;

            if (page <= 0) {
                page = 1;
            }
//            int items_per_page = 20;
//            // build query
//            int offset = (page - 1) * items_per_page;
//            String limit = offset + "," + items_per_page;
            //mLog.debug(" Item order history limit: " + limit);
            String typeMoney = aSession.getRealMoney();
            if (req.typeMoney == 1) {
                typeMoney = "realmoney";
            } else if (req.typeMoney == 0) {
                typeMoney = "money";
            }
            //mLog.debug("typeMoney:" + typeMoney + " req.typeMoney :"+req.typeMoney);
            ArrayList<LogvascEntity> temp = (ArrayList<LogvascEntity>) LogVasObj.getLogVasc(uid, page, typeMoney);

            //mLog.debug(" Item Order List size " + temp.size());

            response.setSuccess(ResponseCode.SUCCESS, temp);
            response.session = aSession;
            aSession.write(response);

        } catch (Throwable t) {
            response.setFailure(ResponseCode.FAILURE, "Có lỗi xảy ra.");
            aResPkg.addPackage(aResPkg);
        }

        return 1;
    }
}
