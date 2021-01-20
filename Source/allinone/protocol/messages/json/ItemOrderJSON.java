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
import allinone.protocol.messages.ItemOrderRequest;
import allinone.protocol.messages.ItemOrderResponse;

public class ItemOrderJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(ItemOrderJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            // request data
            JSONObject jsonData = (JSONObject) aEncodedObj;
            ItemOrderRequest req = (ItemOrderRequest) aDecodingObj;
            //req.itemId = jsonData.getLong("v");
            String[] arr = jsonData.getString("v").split(AIOConstants.SEPERATOR_BYTE_1);
            req.itemId = Long.parseLong(arr[0]);
            if(arr.length > 1){
                req.pass = arr[1];
            }
            return true;

        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();

            ItemOrderResponse read = (ItemOrderResponse) aResponseMessage;

            StringBuilder sb = new StringBuilder();

            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(read.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);

            if (read.mCode == ResponseCode.FAILURE) {
                sb.append(read.mErrorMsg);
//                        .append(AIOConstants.SEPERATOR_BYTE_1);
//                sb.append(read.errorCode);
            } else {
                sb.append(read.value);
            }

            encodingObj.put("v", sb.toString());

            return encodingObj;

        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
