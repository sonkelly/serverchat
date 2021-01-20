package allinone.protocol.messages.json;

import java.util.Vector;

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


import allinone.protocol.messages.GetFreeFriendListRequest;
import allinone.protocol.messages.GetFreeFriendListResponse;

public class GetFreeFriendListJSON implements IMessageProtocol {

    private final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(GetFreeFriendListJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            GetFreeFriendListRequest getFreeFriend = (GetFreeFriendListRequest) aDecodingObj;

            String s = jsonData.getString("v");
            String[] arr = s.split(AIOConstants.SEPERATOR_BYTE_1);
            if (arr.length > 0) {
                getFreeFriend.type = Integer.parseInt(arr[0]);
               
              
            }

            return true;
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }

    }

    private String data(Vector<UserEntity> us) {
        StringBuilder sb = new StringBuilder();
        for (UserEntity u : us) {
            sb.append(u.mUid).append(AIOConstants.SEPERATOR_BYTE_1);
            if (!u.viewName.equals("")) {
                sb.append(u.viewName).append(AIOConstants.SEPERATOR_BYTE_1);
            } else {
                sb.append(u.mUsername).append(AIOConstants.SEPERATOR_BYTE_1);
            }

            sb.append(u.money).append(AIOConstants.SEPERATOR_BYTE_2);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            GetFreeFriendListResponse getFrientList = (GetFreeFriendListResponse) aResponseMessage;
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(getFrientList.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            if (getFrientList.mCode == ResponseCode.FAILURE) {
                sb.append(getFrientList.mErrorMsg);
            } else {
                sb.append(data(getFrientList.mFrientList));
            }
            encodingObj.put("v", sb.toString());
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
