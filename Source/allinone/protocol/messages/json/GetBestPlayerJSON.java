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
import allinone.data.UserEntity;
import allinone.protocol.messages.GetBestPlayerRequest;
import allinone.protocol.messages.GetBestPlayerResponse;

public class GetBestPlayerJSON implements IMessageProtocol {

    private final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(GetBestPlayerJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {

            try {
                // request data
                JSONObject jsonData = (JSONObject) aEncodedObj;
                GetBestPlayerRequest req = (GetBestPlayerRequest) aDecodingObj;

                req.type = jsonData.getInt("v");

                if (req.type <= 0) {
                    req.type = 1; // default get rich
                }

                return true;

            } catch (Throwable t) {
                mLog.error("[DECODER] " + aDecodingObj.getID(), t);
                return false;
            }

        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    private String finalProtocol(GetBestPlayerResponse getFrientList) {
        StringBuilder sb = new StringBuilder();
        for (UserEntity userEntity : getFrientList.mBestPlayerList) {
            sb.append(userEntity.mUid).append(
                    AIOConstants.SEPERATOR_BYTE_1);
            sb.append(userEntity.viewName).append(
                    AIOConstants.SEPERATOR_BYTE_1);
            sb.append(userEntity.rank).append(
                    AIOConstants.SEPERATOR_BYTE_1);
            sb.append(userEntity.avatarID).append(
                    AIOConstants.SEPERATOR_BYTE_1);
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {

        try {

            JSONObject encodingObj = new JSONObject();
            GetBestPlayerResponse response = (GetBestPlayerResponse) aResponseMessage;

            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(response.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (response.mCode == ResponseCode.FAILURE) {
                sb.append(response.mErrorMsg);
            } else {
                sb.append(response.type).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(response.ranking).append(AIOConstants.SEPERATOR_BYTE_1);
                
                    sb.append(response.viewname).append(AIOConstants.SEPERATOR_BYTE_1);
              
                
                sb.append(response.rankData).append(AIOConstants.SEPERATOR_BYTE_2);
                sb.append(finalProtocol(response));
            }
            encodingObj.put("v", sb.toString());

            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
