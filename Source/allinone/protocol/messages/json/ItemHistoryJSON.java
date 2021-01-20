package allinone.protocol.messages.json;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.ItemOrderEntity;
import allinone.data.ResponseCode;
import allinone.protocol.messages.ItemHistoryRequest;
import allinone.protocol.messages.ItemHistoryResponse;

public class ItemHistoryJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(ItemHistoryJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        //return true;
        try {
            // request data
            JSONObject jsonData = (JSONObject) aEncodedObj;
            ItemHistoryRequest req = (ItemHistoryRequest) aDecodingObj;
            req.page = jsonData.getInt("v");
            return true;

        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return true;
        }
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            ItemHistoryResponse send = (ItemHistoryResponse) aResponseMessage;

            StringBuilder sb = new StringBuilder();

            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(send.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);

            if (send.mCode == ResponseCode.FAILURE) {
                sb.append(send.errMgs);
            } else {
                int size = send.data.size();
                for (int i = 0; i < size; i++) {
                    ItemOrderEntity m = send.data.get(i);
                    sb.append(m.getId()).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(m.getItemId()).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(m.getName().trim()).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(m.getPrice()).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(m.getStatus()).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(m.getComment()).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(m.getOrderDate()).append(AIOConstants.SEPERATOR_BYTE_2);
                }

                if (size > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }

            }

            encodingObj.put("v", sb.toString());
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
