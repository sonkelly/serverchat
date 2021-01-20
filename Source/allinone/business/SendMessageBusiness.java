package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.protocol.messages.SendMessageResponse;

public class SendMessageBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(SendMessageBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, 
    		IResponsePackage aResPkg) {
    	
        mLog.debug("[Send Mess]: Catch");
        
        MessageFactory msgFactory = aSession.getMessageFactory();
        
        SendMessageResponse sendRespone = (SendMessageResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        
        sendRespone.session = aSession;
        
//        try {
//        	
//            SendMessageRequest rqHa = (SendMessageRequest) aReqMsg;
//            long uid = aSession.getUID();
//            String mess = rqHa.message;
//            long d_uid = rqHa.dUID;
//            long s_uid = uid;
//            String title = rqHa.title;
//            if(aSession.isMXHDevice())
//            {
//                MessageDB db = new MessageDB();
//                db.insertMessage(s_uid, rqHa.message, d_uid, rqHa.title);
//                CacheUserInfo cache = new CacheUserInfo();
//                cache.deleteCacheMessage(d_uid);
//            }
//            else
//            {
//                DatabaseDriver.sendMess(mess, s_uid, d_uid,title);
//            }
//                resHa.session = aSession;
            sendRespone.message = "Bạn bị đẩy khỏi game, do có login trên thiết bị khác";
        	sendRespone.setSuccess(ResponseCode.SUCCESS);

//        } catch (Throwable t) {
//            t.printStackTrace();
//            resHa.setFailure(ResponseCode.FAILURE, "Có lỗi xảy ra.");
//        }finally {
//        	if(resHa != null){
        		
           aResPkg.addMessage(sendRespone);
        		
//        	}
//        }
        return 1;
    }
}
