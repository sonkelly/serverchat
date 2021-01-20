package allinone.protocol.messages.json;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.PostEntity;
import allinone.data.ResponseCode;
import allinone.protocol.messages.GetPostListResponse;

public class GetPostListJSON implements IMessageProtocol {

    private final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetPostListJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            return true;
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            // put response data into json object
            encodingObj.put("mid", aResponseMessage.getID());
            // cast response obj
            GetPostListResponse getStatusList = (GetPostListResponse) aResponseMessage;
            encodingObj.put("code", getStatusList.mCode);
            if (getStatusList.mCode == ResponseCode.FAILURE) {
            } else if (getStatusList.mCode == ResponseCode.SUCCESS) {
                JSONArray arrRooms = new JSONArray();
                if (getStatusList.mPostList != null) {
                    for (PostEntity postEntity : getStatusList.mPostList) {
                        JSONObject jRoom = new JSONObject();
                        jRoom.put("postID", postEntity.postID);
                        jRoom.put("title", postEntity.title);
                        jRoom.put("avatar", postEntity.avatarID);
                        jRoom.put("content", postEntity.content);
                        jRoom.put("date", postEntity.postDate);
                        jRoom.put("isNew", postEntity.isNewComment);
                        arrRooms.put(jRoom);
                    }
                }
                encodingObj.put("post_list", arrRooms);
            }
            // response encoded obj
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
