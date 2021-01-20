package allinone.business;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.protocol.messages.ForgotPasswordRequest;
import allinone.protocol.messages.ForgotPasswordResponse;

public class ForgotPasswordBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(ForgotPasswordBusiness.class);
@Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
//        mLog.debug("[BET] : Catch  ; " + aSession.getUserName());
        MessageFactory msgFactory = aSession.getMessageFactory();
		ForgotPasswordResponse resTransferCash = (ForgotPasswordResponse) msgFactory
				.getResponseMessage(aReqMsg.getID());
        
        try {
                ForgotPasswordRequest rqAddComment = (ForgotPasswordRequest) aReqMsg;
                if(aSession.getByteProtocol()< 1)
                    aSession.setByteProtocol(AIOConstants.PROTOCOL_REFACTOR);
                
                StringBuilder sb = new StringBuilder();
                sb.append(AIOConstants.KEYWORD_FORGOT_PSW).append(" ").append(rqAddComment.userName).append(" ").append(Integer.toString(rqAddComment.partnerId)).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(AIOConstants.DAUSO_FORGOT_PSW);
                resTransferCash.setSuccess(sb.toString());
                aResPkg.addMessage(resTransferCash);
        }
        
        catch (Throwable t) {
                

        } 
        
        return 1;
    }
}
