package allinone.protocol.messages.json;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.ResponseCode;

import allinone.protocol.messages.ChatZoneRequest;
import allinone.protocol.messages.ChatZoneResponse;
import vn.game.entity.ChatZoneUtity;

public class ChatZoneJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(ChatZoneJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            ChatZoneRequest chat = (ChatZoneRequest) aDecodingObj;

            if (jsonData.has("v")) {
                try {
                    String v = jsonData.getString("v");
                    mLog.debug("v" + v);
                    String[] arrValues = v
                            .split(AIOConstants.SEPERATOR_BYTE_1);

                    chat.type = Integer.parseInt(arrValues[0]);
                    chat.pageIndex = Integer.parseInt(arrValues[1]);
                    chat.mMessage = arrValues[2];
                    chat.zoneId = Integer.parseInt(arrValues[3]);
                    return true;
                } catch (Exception ex) {
                    mLog.error(ex.getMessage(), ex);
                }
            }

            return true;
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();

            ChatZoneResponse chat = (ChatZoneResponse) aResponseMessage;
         
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(chat.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            sb.append(Integer.toString(chat.type)).append(AIOConstants.SEPERATOR_BYTE_1);
            if (chat.mCode == ResponseCode.FAILURE) {
                sb.append(chat.mErrorMsg);
            } else {
                if (chat.type == ChatZoneUtity.STATUS_GETLIST) {
                    sb.append(finalProtocolList(chat));
                } else if (chat.type == ChatZoneUtity.STATUS_CHAT) {
                    sb.append(finalProtocol(chat));
                }

            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            } else {
                sb.append(""); // danh sach rong
            }
            encodingObj.put("v", sb.toString());
         
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }

    private String finalProtocol(ChatZoneResponse chat) {
        StringBuilder sb = new StringBuilder();
        sb.append(chat.mUsername).append(AIOConstants.SEPERATOR_BYTE_1);
        sb.append(chat.mMessage);

        return sb.toString();
    }

    private String finalProtocolList(ChatZoneResponse chats) {
        StringBuilder sb = new StringBuilder();
        int size = chats.listChat.size();
       
        for (int j = 0; j < size; j++) {
            ChatZoneUtity chat = chats.listChat.get(j);
            sb.append(chat.viewname).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(chat.msg).append(AIOConstants.SEPERATOR_BYTE_1);
        }
       
        return sb.toString();
    }
}
