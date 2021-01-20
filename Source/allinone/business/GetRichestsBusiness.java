package allinone.business;

import java.util.Vector;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.Messages;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.databaseDriven.DatabaseDriver;
import allinone.protocol.messages.GetRichestsResponse;

public class GetRichestsBusiness extends AbstractBusiness
{

    private static final Logger mLog = 
    	LoggerContext.getLoggerFactory().getLogger(GetRichestsBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg)
    {
        
    	mLog.debug("[GET RICHEST]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetRichestsResponse resGetRichestList = (GetRichestsResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        resGetRichestList.session = aSession;
        
        try
        {
            long uid = aSession.getUID();
            
            mLog.debug("[GET RICHEST]: for" + uid);

            Vector<UserEntity> richests = DatabaseDriver.getRichests();
            
            resGetRichestList.setSuccess(ResponseCode.SUCCESS, richests);
            
        } catch (Throwable t){
            resGetRichestList.setFailure(ResponseCode.FAILURE, Messages.SYSTEM_DELAY);
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally{
            if ((resGetRichestList != null) ){
                aResPkg.addMessage(resGetRichestList);
            }
        }

        return 1;
    }
}
