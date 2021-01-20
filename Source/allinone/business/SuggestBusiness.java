package allinone.business;


import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.databaseDriven.DatabaseDriver;
import allinone.protocol.messages.SuggestRequest;
import allinone.protocol.messages.SuggestResponse;

public class SuggestBusiness extends AbstractBusiness
{

    private static final Logger mLog = 
    	LoggerContext.getLoggerFactory().getLogger(SuggestBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg)
    {
        mLog.debug("[ SUGGEST ]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        SuggestResponse resSuggest = (SuggestResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try
        {
            SuggestRequest rqSuggest = (SuggestRequest) aReqMsg;
            long uid = aSession.getUID();
            String note = rqSuggest.note;
            mLog.debug("[ SUGGEST ]: of" + uid);
            DatabaseDriver.insertSuggestion(uid, note);
            resSuggest.setSuccess(ResponseCode.SUCCESS);
        } catch (Throwable t){
        	resSuggest.setFailure(ResponseCode.FAILURE, "Quá trình góp ý bị lỗi");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally {
        	if(resSuggest != null){
        		aResPkg.addMessage(resSuggest);
        	}
        }
        return 1;
    }
}
