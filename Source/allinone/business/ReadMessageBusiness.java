package allinone.business;

import tools.CacheUserInfo;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.ResponseCode;
import allinone.databaseDriven.DatabaseDriver;
import allinone.databaseDriven.MessageDB;
import allinone.protocol.messages.ReadMessageRequest;
import allinone.protocol.messages.ReadMessageResponse;

public class ReadMessageBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(ReadMessageBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[ReadMessage] : Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        ReadMessageResponse resRead =
                (ReadMessageResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        resRead.session = aSession;
        
        try {
            ReadMessageRequest req = (ReadMessageRequest) aReqMsg;
            long messID = req.messID;
            StringBuilder sb = new StringBuilder();
            if(aSession.isMXHDevice())
            {
                MessageDB db = new MessageDB();
                String detail = db.readMessage(messID);
                
                
                CacheUserInfo cache = new CacheUserInfo();
                cache.deleteCacheMessage(aSession.getUID());
                
                
                sb.append(messID).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(detail);
                
            }
            else
            {
                DatabaseDriver.readMessage(messID);
            }
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
}
