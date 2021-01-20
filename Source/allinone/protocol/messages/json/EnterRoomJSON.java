package allinone.protocol.messages.json;

import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.MessagesID;
import allinone.data.ResponseCode;
import allinone.data.SimpleTable;
import allinone.protocol.messages.EnterRoomRequest;
import allinone.protocol.messages.EnterRoomResponse;

public class EnterRoomJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
            EnterRoomJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
            throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            EnterRoomRequest enterRoom = (EnterRoomRequest) aDecodingObj;
            if (jsonData.has("v")) {
                enterRoom.roomID = jsonData.getInt("v");
                return true;
            }

            enterRoom.roomID = jsonData.getInt("idRoom");
            return true;
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    private void getMobileEnterRoomValue(EnterRoomResponse enterRoom, StringBuilder sb) {
//            StringBuilder sb = new StringBuilder();
//            sb.append(Integer.toString(MessagesID.EnterRoom)).append(AIOConstants.SEPERATOR_BYTE_1);
//            sb.append(Integer.toString(enterRoom.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);

        if (enterRoom.mCode == ResponseCode.FAILURE) {
            sb.append(enterRoom.mErrorMsg);
        } else {
            List<SimpleTable> tbls = enterRoom.tables;
            int tableSize = 0;
            if (tbls != null) {
                tableSize = tbls.size();

                for (int i = 0; i < tableSize; i++) {
                    SimpleTable table = tbls.get(i);
                    if (table != null) {
                        sb.append(table.getTableIndex()).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(table.getTableSize()).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(table.firstCashBet).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(table.matchID).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(table.maximumPlayer).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(table.isPlaying ? "1" : "0").append(AIOConstants.SEPERATOR_BYTE_2);
                    }
                }
            }

            if (tableSize > 0) {

                sb.deleteCharAt(sb.length() - 1);
            }
        }


    }

    private void getFlashEnterRoomValue(EnterRoomResponse enterRoom, StringBuilder sb) {
//            StringBuilder sb = new StringBuilder();
//            sb.append(Integer.toString(MessagesID.EnterRoom)).append(AIOConstants.SEPERATOR_BYTE_1);
//            sb.append(Integer.toString(enterRoom.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);

        if (enterRoom.mCode == ResponseCode.FAILURE) {
            sb.append(enterRoom.mErrorMsg);
        } else {
            List<SimpleTable> tbls = enterRoom.tables;
            int tableSize = 0;
            if (tbls != null) {
                tableSize = tbls.size();

                for (int i = 0; i < tableSize; i++) {
                    SimpleTable table = tbls.get(i);
                    if (table != null) {
                        sb.append(table.getTableIndex()).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(table.getTableSize()).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(table.firstCashBet).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(table.matchID).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(table.name).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(table.owner.username).append(AIOConstants.SEPERATOR_BYTE_2);

                    }
                }
            }

            if (tableSize > 0) {
//                    System.out.println("capacity " + sb.length());
                sb.deleteCharAt(sb.length() - 1);
            }
        }

    }

    @Override
    public Object encode(IResponseMessage aResponseMessage)
            throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            EnterRoomResponse enterRoom = (EnterRoomResponse) aResponseMessage;

            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(MessagesID.EnterRoom)).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(enterRoom.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);

            if (enterRoom.session.isMobileDevice()) {
                getMobileEnterRoomValue(enterRoom, sb);
                encodingObj.put("v", sb.toString());
            } else {
                getFlashEnterRoomValue(enterRoom, sb);
                encodingObj.put("v", sb.toString());
            }
            return encodingObj;

        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}