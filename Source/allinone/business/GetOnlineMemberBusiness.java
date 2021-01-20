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
import allinone.data.Messages;
import allinone.data.ResponseCode;
import allinone.protocol.messages.GetOnlineMemberResponse;
import allinone.server.Server;


/**
 *
 * @author mcb
 */
public class GetOnlineMemberBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetOnlineMemberBusiness.class);
    
    
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
//        mLog.debug("get albums");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetOnlineMemberResponse resChat = (GetOnlineMemberResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
//            GetOnlineMemberRequest request = (GetOnlineMemberRequest)aReqMsg;
            resChat.mCode = ResponseCode.SUCCESS;       
            
            ChatRoom chatRoom = Server.getChatRoomZone().findChatRoom(aSession.getChatRoom());
            if(chatRoom == null)
            {
                throw new BusinessException(Messages.INVALID_CHAT_ROOM);
            }
            resChat.value = chatRoom.getOnlinePlayer(aSession);
            //send to sender first
            aSession.write(resChat);
           
            
        } 
        catch(BusinessException ex)
        {
            mLog.warn(ex.getMessage());
            resChat.setFailure(ex.getMessage());
            
        }
        catch (ServerException ex) {
            mLog.error(ex.getMessage(), ex);
                        
        }
        
             
        return 1;
    }
}
