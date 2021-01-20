package allinone.business;

import org.slf4j.Logger;

import tools.CacheUserInfo;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.ResponseCode;
import allinone.databaseDriven.MessageDB;
import allinone.protocol.messages.PrivateChatRequest;
import allinone.protocol.messages.PrivateChatResponse;

public class PrivateChatBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(PrivateChatBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        int rtn = PROCESS_FAILURE;
        MessageFactory msgFactory = aSession.getMessageFactory();
        PrivateChatResponse resChat =
                (PrivateChatResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        mLog.debug("[PRIVATE CHAT]: Catch");
        try {
            PrivateChatRequest rqChat = (PrivateChatRequest) aReqMsg;
            String message = rqChat.mMessage;
            long sourceID = aSession.getUID();
            long destID = rqChat.destUid;

//            ISession buddySession = aSession.getManager().findSession(destID);
            ISession buddySession = aSession.getManager().findPrvChatSession(destID);
//            ISession sourceSesstion = aSession.getManager().findSession(sourceID);
//            UserEntity usrEntity = CacheUserInfo.getCacheUserInfo(sourceID);
            if(aSession.isMXHDevice())
            {
//                if (buddySession != null && buddySession.getChatRoom()>0) {
                if (buddySession != null) {
                    if(buddySession.getChatRoom()>0 || buddySession.getByteProtocol()> AIOConstants.PROTOCOL_MXH)
                    {
                        if(buddySession.getRoom()!= null && buddySession.getRoom().isPlaying() 
                                && buddySession.getDeviceType() != AIOConstants.IPHONE_DEVICE)
                        {
                            resChat.setFailure(ResponseCode.FAILURE, buddySession.getUserName() + " đang chơi game rồi");
                            // Send message to buddy
                            aSession.write(resChat);
                        
                        }
                        else
                        {
                            resChat.setSuccess(ResponseCode.SUCCESS, sourceID, message, aSession.getUserName());
                            // Send message to buddy
                            buddySession.write(resChat);
                        }
                    }
                } else {
                    if(aSession.getByteProtocol()> AIOConstants.PROTOCOL_MXH)
                    {
                        CacheUserInfo cache = new CacheUserInfo();
                        String destName = cache.getUserInfo(destID,aSession.getRealMoney()).mUsername;
                        resChat.setFailure(ResponseCode.FAILURE, destName + " đã offline rồi");
                      // Send message to buddy
                        aSession.write(resChat);
                    }
                    
                    
                    
                        MessageDB db = new MessageDB();
                        db.insertMessage(sourceID, message, destID, "");
                    

                }
            }
            else
            {
                if (buddySession != null && buddySession.getChatRoom()>0) {
                    resChat.setSuccess(ResponseCode.SUCCESS, sourceID, message, aSession.getUserName());
                    // Send message to buddy
                    buddySession.write(resChat);
                } 
            }
            

            rtn = PROCESS_OK;

        } catch (Throwable t) {

            resChat.setFailure(ResponseCode.FAILURE, "Process Error!");
            //aSession.setLoggedIn(false);
            rtn = PROCESS_OK;
            // log this error
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally {
        }

        return rtn;
    }
}
