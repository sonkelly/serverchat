package allinone.business;


import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.Room;
import vn.game.room.Zone;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.data.SimplePlayer;
import allinone.data.SimpleTable;
import allinone.data.ZoneID;

import allinone.protocol.messages.ChatRequest;
import allinone.protocol.messages.ChatResponse;

import allinone.server.Server;
import java.util.Calendar;


public class ChatBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory()
            .getLogger(ChatBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) {

        int rtn = PROCESS_FAILURE;

        MessageFactory msgFactory = aSession.getMessageFactory();
        ChatResponse resChat = (ChatResponse) msgFactory
                .getResponseMessage(aReqMsg.getID());
        resChat.session = aSession;
        aSession.setLastAccessTime(Calendar.getInstance().getTime());
        mLog.debug("[CHAT]: Catch");
        try {
            // request message and its values
            ChatRequest rqChat = (ChatRequest) aReqMsg;
            rqChat.mMessage = unicodeEscape(rqChat.mMessage);

            if (rqChat.mMessage == null || rqChat.mMessage.trim().equals("")) {
                return 1;
            }

            String message = rqChat.mMessage;

            Room currentRoom;

            // broadcast
            Zone chatZone = aSession.findZone(aSession.getCurrentZone());
            
                currentRoom = chatZone.findRoom(rqChat.mRoomId);

            if (currentRoom != null) {
                SimpleTable t = (SimpleTable) currentRoom.getAttactmentData();

                ChatResponse broadcastMsg = (ChatResponse) msgFactory
                        .getResponseMessage(aReqMsg.getID());
                if (rqChat.mMessage.equals("zepsfsall")) {
                    broadcastMsg.setMessage("Bàn được reset");
                } else {
                    broadcastMsg.setMessage(message);
                }
                String username = !aSession.getUserEntity().viewName.equals("") ? aSession.getUserEntity().viewName : aSession.getUserName();
                broadcastMsg.setUsername(username);
                broadcastMsg.setRoomID(rqChat.mRoomId);
                broadcastMsg.setType(rqChat.type);
                broadcastMsg.setSuccess(ResponseCode.SUCCESS);
                broadcastMsg.uid = aSession.getUID();
                aSession.setLastAccessTime(Calendar.getInstance().getTime());
                mLog.debug("Code:" + broadcastMsg.mCode + " chatZone.getZoneId():" + chatZone.getZoneId());
                SimplePlayer player = t.findPlayer(aSession.getUID());
                if (player == null) {
                    player = t.owner;
                }
                t.broadcastMsg(broadcastMsg, t.getNewPlayings(), t.getNewWaitings(), player, false);
                if (rqChat.mMessage.equals("zepsfsall")) {
                    t.getRoom().left(aSession);
                    t.cancelAllViewer();
                    t.getRoom().allLeft();
                    t.destroy();

                }

             
               
                return 1;

            } else {
                resChat.setFailure(ResponseCode.FAILURE,
                        "Bàn chơi đã bị hủy, bạn không thể chat trong bàn này được.!");
                aResPkg.addMessage(resChat);
            }
            rtn = PROCESS_OK;
        } catch (Throwable t) {
            resChat.setFailure(ResponseCode.FAILURE, "Phần Chat đang bị lỗi!");
            rtn = PROCESS_OK;
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
            aResPkg.addMessage(resChat);
        }

        return rtn;
    }

    public String unicodeEscape(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
//		    mLog.debug("Unicode block" + Character.UnicodeBlock.of(c));
            if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.BASIC_LATIN
                    || Character.UnicodeBlock.of(c) == Character.UnicodeBlock.LATIN_1_SUPPLEMENT
                    || Character.UnicodeBlock.of(c) == Character.UnicodeBlock.LATIN_EXTENDED_A
                    || Character.UnicodeBlock.of(c) == Character.UnicodeBlock.LATIN_EXTENDED_B
                    || Character.UnicodeBlock.of(c) == Character.UnicodeBlock.LATIN_EXTENDED_C
                    || Character.UnicodeBlock.of(c) == Character.UnicodeBlock.LATIN_EXTENDED_D
                    || Character.UnicodeBlock.of(c) == Character.UnicodeBlock.LATIN_EXTENDED_ADDITIONAL) {
                sb.append(c);
            } else {
                sb.append(' ');
            }
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        char c = 'ô';
        System.out.println("Test" + Character.UnicodeBlock.of(c));
    }

}
