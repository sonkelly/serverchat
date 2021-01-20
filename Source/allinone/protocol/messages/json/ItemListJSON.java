package allinone.protocol.messages.json;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.ItemEntity;
import allinone.data.ResponseCode;
import allinone.protocol.messages.ItemListRequest;
import allinone.protocol.messages.ItemListResponse;

public class ItemListJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(ItemListJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {

            JSONObject jsonData = (JSONObject) aEncodedObj;
            ItemListRequest req = (ItemListRequest) aDecodingObj;

            if (jsonData.has("v")) {
                req.itemType = jsonData.getInt("v");
                return true;
            }

            return false;

        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            ItemListResponse send = (ItemListResponse) aResponseMessage;

            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(send.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            sb.append(Integer.toString(send.itemType)).append(AIOConstants.SEPERATOR_BYTE_3);
            //sb.append(Integer.toString(send.cardSize)).append(AIOConstants.SEPERATOR_BYTE_1);
            //sb.append(Integer.toString(send.itemSize)).append(AIOConstants.SEPERATOR_BYTE_2);

            if (send.mCode == ResponseCode.FAILURE) {
                sb.append(send.errMgs);
            } else {
                if (send.data != null) {
                    int size = send.data.size();
                    for (int i = 0; i < size; i++) {
                        ItemEntity m = send.data.get(i);
                        sb.append(m.getItemId()).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(m.getName().trim()).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(m.getPrice()).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(m.getImage().trim()).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(m.getPriceChange()).append(AIOConstants.SEPERATOR_BYTE_2);
                    }
                    if (size > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                } else {
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
