package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.MessagesID;
import allinone.data.ResponseCode;
import allinone.data.ZoneID;
import allinone.databaseDriven.RoomDB;
import allinone.protocol.messages.ListPhongRequest;
import allinone.protocol.messages.ListPhongResponse;
import allinone.protocol.messages.UpdateCashRequest;
import java.util.ArrayList;
import java.util.Calendar;
import vn.game.common.SessionUtils;

public class ListPhongBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(ListPhongBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        MessageFactory msgFactory = aSession.getMessageFactory();
        ListPhongResponse resEnter = (ListPhongResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        //mLog.debug("[ENTER ZONE]: Catch");
        String zoneName = "#";

//        if (!SessionUtils.checkSessionDB(aSession)) {
//            return 0;
//        }
        try {

            ListPhongRequest rqEnter = (ListPhongRequest) aReqMsg;
            int zoneID = rqEnter.zoneID;

            aSession.setLastAccessTime(Calendar.getInstance().getTime());

            resEnter.session = aSession;
            ArrayList<Integer> al = new ArrayList<Integer>();
            if (aSession.getRealMoney().equals("realmoney")) {
                al.add(500);
                al.add(1000);
                al.add(2000);
                al.add(5000);
                al.add(10000);
                al.add(20000);
                al.add(50000);
                al.add(100000);
                al.add(200000);
                al.add(500000);
            } else {

                al.add(1000);
                al.add(2000);
                al.add(5000);
                al.add(10000);
                al.add(20000);
                al.add(50000);
                al.add(100000);
                al.add(200000);
                al.add(500000);
                al.add(1000000);
            }
            resEnter.listPhong = al;
            resEnter.setSuccess(ResponseCode.SUCCESS);

            aSession.setPhongID(0);

            aResPkg.addMessage(resEnter);

        } catch (Throwable t) {
            resEnter.setFailure(ResponseCode.FAILURE, "Hiện tại không vào được game " + zoneName + " này!");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
            aResPkg.addMessage(resEnter);
        }
        return 1;
    }

    public void sendListPhong(ISession aSession) {
        //mLog.debug("vaoday  sendListPhong");
        MessageFactory msgFactory = aSession.getMessageFactory();
        ListPhongResponse resEnter = (ListPhongResponse) msgFactory.getResponseMessage(MessagesID.LIST_PHONG);
        ArrayList<Integer> al = new ArrayList<Integer>();
        if (aSession.getRealMoney().equals("realmoney")) {
            al.add(500);
            al.add(1000);
            al.add(2000);
            al.add(5000);
            al.add(10000);
            al.add(20000);
            al.add(50000);
            al.add(100000);
            al.add(500000);
        } else {

            al.add(1000);
            al.add(2000);
            al.add(5000);
            al.add(10000);
            al.add(20000);
            al.add(50000);
            al.add(100000);
            al.add(200000);
            al.add(1000000);
        }
        resEnter.listPhong = al;
        resEnter.setSuccess(ResponseCode.SUCCESS);
        resEnter.setSession(aSession);
        try {
            aSession.write(resEnter);
        } catch (ServerException ex) {
            mLog.error("sendMsgRefund ServerException" + ex.getMessage(), ex);
        }

    }
}
