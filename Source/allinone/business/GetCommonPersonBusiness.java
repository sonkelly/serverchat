/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;


import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.databaseDriven.InfoDB;
import allinone.protocol.messages.GetCommonPersonRequest;
import allinone.protocol.messages.GetCommonPersonResponse;



/**
 *
 * @author mcb
 */
public class GetCommonPersonBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetCommonPersonBusiness.class);
    
    
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
//        mLog.debug("get albums");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetCommonPersonResponse resChat = (GetCommonPersonResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
            GetCommonPersonRequest request = (GetCommonPersonRequest)aReqMsg;
            resChat.mCode = ResponseCode.SUCCESS;      
            
            
            
            resChat.value = InfoDB.getPersonCommonInfo();
            
            //send to sender first
            aSession.write(resChat);
           
            
        } 
        
        catch (ServerException ex) {
            mLog.error(ex.getMessage(), ex);
                        
        }
        
             
        return 1;
    }
}
