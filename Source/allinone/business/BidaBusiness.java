package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;

import vn.game.session.ISession;
import allinone.data.ResponseCode;

import allinone.data.ZoneID;
import allinone.databaseDriven.ChatZoneDB;

import allinone.protocol.messages.ChatZoneRequest;
import allinone.protocol.messages.ChatZoneResponse;
import allinone.server.Server;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Enumeration;

import vn.game.common.ServerException;
import vn.game.entity.ChatZoneUtity;
import vn.game.session.SessionManager;

public class BidaBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory()
            .getLogger(BidaBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) {

        int rtn = PROCESS_FAILURE;

        MessageFactory msgFactory = aSession.getMessageFactory();
        ChatZoneResponse resChat = (ChatZoneResponse) msgFactory
                .getResponseMessage(aReqMsg.getID());
        resChat.session = aSession;
        aSession.setLastAccessTime(Calendar.getInstance().getTime());
        mLog.debug("[BidaBusiness]");
        try {
            // request message and its values
            ChatZoneRequest rqChat = (ChatZoneRequest) aReqMsg;
            rqChat.mMessage = unicodeEscape(rqChat.mMessage);

            if (rqChat.mMessage == null || rqChat.mMessage.trim().equals("")) {
                return 1;
            }
          
            if (rqChat.type == 1) {//get list cue
                this.processGetListCue(aSession, aReqMsg, aResPkg, resChat);
            } else if (rqChat.type == 1) {//bat dau chat
               // this.processChat(aSession, aReqMsg, aResPkg, resChat, zoneID);
            }
            rtn = PROCESS_OK;
        } catch (Throwable t) {

            rtn = PROCESS_OK;

        }

        return rtn;
    }

    public void processGetListCue(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg, ChatZoneResponse resChat) {
        try {

            ChatZoneRequest rqChat = (ChatZoneRequest) aReqMsg;

           
            ChatZoneDB chatDbObj = new ChatZoneDB();
//            ArrayList<ChatZoneUtity> listChat = chatDbObj.getChats(rqChat.pageIndex, zoneID);
//            resChat.listChat = listChat;
//            resChat.setSuccessList(1, listChat);

            aResPkg.addMessage(resChat);

        } catch (Exception ex) {
            resChat.setFailure(ResponseCode.FAILURE, "Phần Chat đang bị lỗi!");

            aResPkg.addMessage(resChat);

        }
    }

    //process chat 
    public void processChat(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg, ChatZoneResponse resChat, int zoneID) {

        try {

            ChatZoneRequest rqChat = (ChatZoneRequest) aReqMsg;
            //add log chat to db
            ChatZoneDB chatDBObj = new ChatZoneDB();
            chatDBObj.addChat(zoneID, aSession.getUID(), aSession.getUserEntity().viewName, rqChat.mMessage);
            //end chat to db
            //MessageFactory msgFactory = aSession.getMessageFactory();
            SessionManager sm = Server.getWorker().getmSessionMgr();
            Enumeration<ISession> values;
            values = sm.getmSessions().elements();

            while (values.hasMoreElements()) {
                mLog.debug("00000:" + zoneID);
                ISession session = values.nextElement();
                if (session == null || session.realDead()) {
                    continue;
                }
                //bo dung cho chat tong cua bida, xoso
                //ko can join zone, van nhan duoc chat
//                if (session.getCurrentZone() == ZoneID.BIDA || zoneID == ZoneID.XO_SO) {//chi dung cho bida
//                    mLog.debug("1111");
                ChatZoneResponse resChatUser = (ChatZoneResponse) session.getMessageFactory().getResponseMessage(aReqMsg.getID());
                resChatUser.setSuccess(1, rqChat.mMessage, rqChat.type, aSession.getUserEntity().viewName);
                try {
                    session.write(resChatUser);
                } catch (ServerException ex) {
                    mLog.debug("chat error" + ex.getMessage());
                }
                //}

            }

        } catch (Exception ex) {
            resChat.setFailure(ResponseCode.FAILURE, "Phần Chat đang bị lỗi!");

            aResPkg.addMessage(resChat);

        }
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
