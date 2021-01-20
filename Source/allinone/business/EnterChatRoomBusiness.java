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
import allinone.chat.data.ChatRoom;
import allinone.data.ResponseCode;
import allinone.protocol.messages.EnterChatRoomRequest;
import allinone.protocol.messages.EnterChatRoomResponse;
import allinone.server.Server;

/**
 *
 * @author mcb
 */
public class EnterChatRoomBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(EnterChatRoomBusiness.class);
    
    
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
//        mLog.debug("get albums");
        MessageFactory msgFactory = aSession.getMessageFactory();
        EnterChatRoomResponse resRooms = (EnterChatRoomResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
            EnterChatRoomRequest request = (EnterChatRoomRequest)aReqMsg;
            resRooms.mCode = ResponseCode.SUCCESS;       
            ChatRoom chatRoom = Server.getChatRoomZone().findChatRoom(request.chatRoomId);
            chatRoom.enter(aSession);
//            aSession.setCurrentZone(0);
            aSession.setCurrPosition(null);
            
            resRooms.value = chatRoom.getChatRoomHistory();
            aSession.write(resRooms);
            
            
//            //send online play in queue
//            GetOnlineMemberResponse onlineRes = (EnterChatRoomResponse) msgFactory.getResponseMessage(MessagesID.GET_ONLINE_MEMBER);
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
