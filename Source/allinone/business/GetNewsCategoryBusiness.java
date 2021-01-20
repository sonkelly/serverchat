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
import allinone.databaseDriven.NewsCategoryDB;
import allinone.protocol.messages.GetNewsCategoryResponse;




/**
 *
 * @author mcb
 */
public class GetNewsCategoryBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetNewsCategoryBusiness.class);
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("get news category");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetNewsCategoryResponse resalbum = (GetNewsCategoryResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
            
            resalbum.mCode = ResponseCode.SUCCESS;  
            resalbum.value = NewsCategoryDB.getStrCategories();
            
        } 
        
        finally
        {
            if (resalbum!= null)
            {
                try {
                    aSession.write(resalbum);
                } catch (ServerException ex) {
                    mLog.error(ex.getMessage(), ex);
                }
                        
            }
        }        
        return 1;
    }
}
