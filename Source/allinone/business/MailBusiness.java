package allinone.business;

import java.util.ArrayList;

import org.slf4j.Logger;

import tools.CacheEventInfo;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.Mail;
import allinone.data.ResponseCode;
import allinone.databaseDriven.InfoDB;
import allinone.databaseDriven.MailDB;
import allinone.protocol.messages.MailResponse;
import allinone.protocol.messages.MailRequest;
import java.util.Calendar;

public class MailBusiness extends AbstractBusiness {

    private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(MailBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) {
        mLog.debug("[Get list mail]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        MailResponse response = (MailResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        MailRequest req = (MailRequest) aReqMsg;
        try {
            long s_uid = aSession.getUID();

            ArrayList<Mail> temp = null;

//            CacheEventInfo cache = new CacheEventInfo();
//            temp = ( ArrayList<Mail>)cache.getMail(s_uid);
            int page = req.pageIndex;

            if (page <= 0) {
                page = 1;
            }
            int items_per_page = 20;
            // build query
            int offset = (page - 1) * items_per_page;
            String limit = offset + "," + items_per_page;
            mLog.debug(" Item order history limit: " + limit);
            MailDB db = new MailDB();
            temp = (ArrayList<Mail>) db.getListMail(s_uid, limit);
            mLog.debug(" Mail size " + temp.size());

            response.setSuccess(ResponseCode.SUCCESS, temp);
            aSession.setLastAccessTime(Calendar.getInstance().getTime());
            response.session = aSession;
            aSession.write(response);

        } catch (Throwable t) {
            response.setFailure(ResponseCode.FAILURE, "Có lỗi xảy ra.");
            aResPkg.addPackage(aResPkg);
        }

        return 1;
    }

//    public static void main(String[] args) {
//        ArrayList<Mail> temp = null;
//        int page = 1;
//
//        if (page <= 0) {
//            page = 1;
//        }
//        int items_per_page = 20;
//        // build query
//        int offset = (page - 1) * items_per_page;
//        String limit = offset + "," + items_per_page;
//
//        InfoDB db = new InfoDB();
//
//        try {
//            temp = (ArrayList<Mail>) db.getListMail(312855, limit);
//
//            for (Mail ett : temp) {
//
//                System.out.println("log:" + ett.title);
//            }
//            //loge = logev.getLogVasc(312855, 1);
//        } catch (Exception ex) {
//           
//        }
//
//    }
}
