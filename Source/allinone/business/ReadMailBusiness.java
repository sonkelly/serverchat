package allinone.business;

import java.util.ArrayList;

import tools.CacheEventInfo;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.Mail;
import allinone.data.ResponseCode;
import allinone.databaseDriven.InfoDB;
import allinone.databaseDriven.MailDB;
import allinone.protocol.messages.ReadMailRequest;
import allinone.protocol.messages.ReadMailResponse;

public class ReadMailBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog = LoggerContext.getLoggerFactory().getLogger(ReadMessageBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[ReadMessage] : Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        ReadMailResponse resRead = (ReadMailResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        resRead.session = aSession;

        try {
            long uid = aSession.getUID();
            ReadMailRequest req = (ReadMailRequest) aReqMsg;
            long mailId = req.mailId;

            StringBuilder sb = new StringBuilder();

            // get mail list
            ArrayList<Mail> temp = null;

//            CacheEventInfo cache = new CacheEventInfo();
//            temp = (ArrayList<Mail>) cache.getMail(uid);
            MailDB maildb = new MailDB();
            Mail mailObj = maildb.getMailDetail(uid, mailId);

            // get mail content and read status
            String mailContent = "";
            int isRead = 0;
            if (mailObj != null) {
                mailContent = mailObj.detail;
                isRead = mailObj.isRead;
            }
            sb.append(mailId).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(mailContent);

            resRead.value = sb.toString();
            resRead.setSuccess(ResponseCode.SUCCESS);

        } catch (Exception e) {
            mLog.error(e.getMessage(), e);
            resRead.setFailure(ResponseCode.FAILURE, "Không thể kết nối đến cơ sở dữ liệu");
        } finally {
            if ((resRead != null)) {
                aResPkg.addMessage(resRead);
            }
        }
        return 1;
    }

    public static void main(String[] args) {
        Mail temp = null;
        int page = 1;

        if (page <= 0) {
            page = 1;
        }
        int items_per_page = 20;
        // build query
        int offset = (page - 1) * items_per_page;
        String limit = offset + "," + items_per_page;

        MailDB db = new MailDB();

        try {
            temp = db.getMailDetail(312855, 1473730);

            System.out.println("log:" + temp.title);

            //loge = logev.getLogVasc(312855, 1);
        } catch (Exception ex) {

        }

    }
}
