package allinone.business;

import allinone.data.MessagesID;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;

import vn.game.session.ISession;
import allinone.data.ResponseCode;

import allinone.protocol.messages.ChangeMoneyTypeRequest;
import allinone.protocol.messages.ChangeMoneyTypeResponse;
import allinone.protocol.messages.ListPhongRequest;
import allinone.protocol.messages.ListPhongResponse;
import allinone.protocol.messages.PlayEnterZoneRequest;
import allinone.protocol.messages.ReconnectResponse;
import java.util.ArrayList;

import java.util.Calendar;
import vn.game.common.ServerException;
import vn.game.protocol.IBusiness;

public class ChangeMoneyTypeBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(ChangeMoneyTypeBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) {

        int rtn = PROCESS_FAILURE;

        MessageFactory msgFactory = aSession.getMessageFactory();
        ChangeMoneyTypeResponse resChange = (ChangeMoneyTypeResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        aSession.setLastAccessTime(Calendar.getInstance().getTime());
        mLog.debug("[CHANGE ChangeMoneyTypeBusiness]");
        resChange.session = aSession;

        try {
            mLog.debug("sess isRealmoney:" + aSession.getRealMoney());
            int moneType = 0;
            if (aSession.getRealMoney().equals("realmoney")) {
                moneType = 1;
            }
            ChangeMoneyTypeRequest rqChange = (ChangeMoneyTypeRequest) aReqMsg;
            if (rqChange.moneType == 0) {
                aSession.setRealMoney("money");
                moneType = 0;
            } else if (rqChange.moneType == 1) {
                aSession.setRealMoney("realmoney");
                moneType = 1;
            }

            rtn = PROCESS_OK;
            resChange.setSuccess(ResponseCode.SUCCESS, moneType);
            aResPkg.addMessage(resChange);
            mLog.debug("sess isRealmoney2:" + aSession.getRealMoney());
            //send list phong
            ListPhongBusiness lsB = new ListPhongBusiness();
            lsB.sendListPhong(aSession);
            //end
        } catch (Throwable t) {
            resChange.setFailure(ResponseCode.FAILURE, "Không thay đổi được cấu hình!");
            rtn = PROCESS_OK;
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
            aResPkg.addMessage(resChange);
        }
        return rtn;
    }

    
}
