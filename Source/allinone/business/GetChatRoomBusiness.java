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
import allinone.protocol.messages.GetChatRoomRequest;
import allinone.protocol.messages.GetChatRoomResponse;
import allinone.server.Server;

/**
 *
 * @author mcb
 */
public class GetChatRoomBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetChatRoomBusiness.class);
    
    private static final int CACHE_VERSION = 3;
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
//        mLog.debug("get albums");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetChatRoomResponse resRooms = (GetChatRoomResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
             GetChatRoomRequest rqChatRoom = (GetChatRoomRequest) aReqMsg;
             
             resRooms.mCode = ResponseCode.SUCCESS;
             
             if(rqChatRoom.cacheVersion != CACHE_VERSION)
             {     
                resRooms.value = Server.getChatRoomZone().getAllRooms(CACHE_VERSION);
             }
             else
             {
                 resRooms.value = Server.getChatRoomZone().getOnlyPlaying();
                 
             }
             
             
                
             aSession.write(resRooms);
            
        } 
        catch (ServerException ex) {
            mLog.error(ex.getMessage(), ex);
        }                
                   
            

            
            
//        }
//        catch(BusinessException ex)
//        {
//            mLog.warn(ex.getMessage());
//            resalbum.setFailure(ex.getMessage());
//            
//        }
             
        return 1;
    }
}
