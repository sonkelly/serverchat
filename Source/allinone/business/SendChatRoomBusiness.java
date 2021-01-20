/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import java.util.Date;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.chat.data.ChatEntity;
import allinone.chat.data.ChatRoom;
import allinone.data.AIOConstants;
import allinone.data.Messages;
import allinone.data.ResponseCode;
import allinone.protocol.messages.SendChatRoomRequest;
import allinone.protocol.messages.SendChatRoomResponse;
import allinone.server.Server;


/**
 *
 * @author mcb
 */
public class SendChatRoomBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(SendChatRoomBusiness.class);
    
    
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
//        mLog.debug("get albums");
        MessageFactory msgFactory = aSession.getMessageFactory();
        SendChatRoomResponse resChat = (SendChatRoomResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
            SendChatRoomRequest request = (SendChatRoomRequest)aReqMsg;
            resChat.mCode = ResponseCode.SUCCESS;       
            
            ChatRoom chatRoom = Server.getChatRoomZone().findChatRoom(aSession.getChatRoom());
            if(chatRoom == null)
            {
                throw new BusinessException(Messages.INVALID_CHAT_ROOM);
            }
            //send to sender first
//            aSession.write(resChat);
            ChatEntity entity = new ChatEntity(request.content, aSession, new Date());
            chatRoom.addMessageHistory(entity);
            
            //broadcast message to another joiner
            SendChatRoomResponse broadcastMsg = (SendChatRoomResponse)resChat.clone(aSession);
            StringBuilder sb = new StringBuilder();
            sb.append(aSession.getUID()).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(aSession.getUserName()).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(request.content);
            broadcastMsg.value =  sb.toString();
            broadcastMsg.mCode = ResponseCode.SUCCESS;
            chatRoom.broadcastWithoutSender(broadcastMsg, aSession);
            
        } 
        catch(BusinessException ex)
        {
            mLog.warn(ex.getMessage());
            resChat.setFailure(ex.getMessage());
            
        }
//        catch (ServerException ex) {
//            mLog.error(ex.getMessage(), ex);
//                        
//        }
        
             
        return 1;
    }
}
