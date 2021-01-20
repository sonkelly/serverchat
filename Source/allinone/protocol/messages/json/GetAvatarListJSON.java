package allinone.protocol.messages.json;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.AvatarEntity;
import allinone.data.HTTPPoster;
import allinone.data.ResponseCode;
import allinone.protocol.messages.GetAvatarListResponse;

public class GetAvatarListJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
            GetAvatarListJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
            throws ServerException {
        return true;
    }

    @Override
    public Object encode(IResponseMessage aResponseMessage)
            throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            GetAvatarListResponse getAvatarList = (GetAvatarListResponse) aResponseMessage;
            if (getAvatarList.session != null && getAvatarList.session.getByteProtocol() > AIOConstants.PROTOCOL_ADVERTISING) {
                StringBuilder sb = new StringBuilder();
                sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Integer.toString(getAvatarList.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
                if (getAvatarList.mCode == ResponseCode.FAILURE) {
                    sb.append(getAvatarList.mErrorMsg);
                } else {

                    for (AvatarEntity avaEntity : getAvatarList.mAvatarList) {
                        sb.append(avaEntity.id).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(avaEntity.avatar).append(AIOConstants.SEPERATOR_BYTE_2);

                    }
                    if (getAvatarList.mAvatarList.size() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                }
                encodingObj.put("v", sb.toString());
                return encodingObj;
            }
            encodingObj.put("mid", aResponseMessage.getID());

            encodingObj.put("code", getAvatarList.mCode);
            if (getAvatarList.mCode == ResponseCode.FAILURE) {
                encodingObj.put("error_msg", getAvatarList.mErrorMsg);
            } else if (getAvatarList.mCode == ResponseCode.SUCCESS) {
                JSONArray arrRooms = new JSONArray();
                if (getAvatarList.mAvatarList != null) {
                    for (AvatarEntity avaEntity : getAvatarList.mAvatarList) {
                        JSONObject jRoom = new JSONObject();
                        jRoom.put("id", avaEntity.id);
                        jRoom.put("avatar", avaEntity.avatar);

                        arrRooms.put(jRoom);
                    }
                }
                encodingObj.put("avatar_list", arrRooms);
            }
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
