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
import allinone.data.ZoneID;
import allinone.protocol.messages.ChangeMoneyTypeRequest;

import allinone.protocol.messages.ChangeMoneyTypeResponse;

public class ChangeMoneyTypeJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
            ChangeMoneyTypeJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
            throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            ChangeMoneyTypeRequest changeS = (ChangeMoneyTypeRequest) aDecodingObj;

            String s = jsonData.getString("v");
            String[] arr = s.split(AIOConstants.SEPERATOR_BYTE_1);
            changeS.moneType = Integer.parseInt(arr[0]);
            
            return true;

        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage)
            throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            ChangeMoneyTypeResponse changeS = (ChangeMoneyTypeResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(changeS.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (changeS.mCode == ResponseCode.FAILURE) {
                sb.append(changeS.mErrorMsg);
            } else {
                sb.append(changeS.moneyType).append(AIOConstants.SEPERATOR_BYTE_1);

               
            }
            encodingObj.put("v", sb.toString());
            return encodingObj;

        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
