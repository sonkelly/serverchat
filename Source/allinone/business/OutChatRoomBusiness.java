/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.chat.data.ChatRoom;
import allinone.data.AIOConstants;
import allinone.data.ResponseCode;
import allinone.protocol.messages.OutChatRoomRequest;
import allinone.protocol.messages.OutChatRoomResponse;
import allinone.server.Server;

/**
 *
 * @author mcb
 */
public class OutChatRoomBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(OutChatRoomBusiness.class);
    
    
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
//        mLog.debug("get albums");
        MessageFactory msgFactory = aSession.getMessageFactory();
        OutChatRoomResponse resRooms = (OutChatRoomResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
            OutChatRoomRequest request = (OutChatRoomRequest)aReqMsg;
            resRooms.mCode = ResponseCode.SUCCESS;       
            ChatRoom chatRoom = Server.getChatRoomZone().findChatRoom(request.chatRoomId);
            if(chatRoom != null)
            {
                chatRoom.out(aSession);
            }
            StringBuilder sb = new StringBuilder();
            sb.append(Long.toString(aSession.getUID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(request.chatRoomId));
            
            resRooms.value = sb.toString();
//            aSession.write(resRooms);
            
            
//            //send online play in queue
//            GetOnlineMemberResponse onlineRes = (EnterChatRoomResponse) msgFactory.getResponseMessage(MessagesID.GET_ONLINE_MEMBER);
        } 
        catch (Exception ex) {
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
