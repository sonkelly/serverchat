package allinone.protocol.messages.json;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import vn.game.room.RoomEntity;
import allinone.data.ResponseCode;
import allinone.data.ZoneID;
import allinone.protocol.messages.GetWaitingListRequest;
import allinone.protocol.messages.GetWaitingListResponse;

public class GetWaitingListJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(GetWaitingListJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            // request data
            JSONObject jsonData = (JSONObject) aEncodedObj;
            // request messsage
            GetWaitingListRequest getWaitingList = (GetWaitingListRequest) aDecodingObj;
            // parsing
            getWaitingList.mOffset = jsonData.getInt("offset");
            getWaitingList.mLength = jsonData.getInt("length");
            getWaitingList.level = jsonData.getInt("level");
            getWaitingList.minLevel = jsonData.getInt("minLevel");
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
            GetWaitingListResponse getWaitingList = (GetWaitingListResponse) aResponseMessage;
            encodingObj.put("code", getWaitingList.mCode);
            if (getWaitingList.mCode == ResponseCode.FAILURE) {
            } else if (getWaitingList.mCode == ResponseCode.SUCCESS) {
                encodingObj.put("num_playing_room", getWaitingList.mNumPlayingRoom);
                // room dummy
                JSONArray arrRooms = new JSONArray();
                if (getWaitingList.mWaitingRooms != null) {
                    for (RoomEntity roomEntity : getWaitingList.mWaitingRooms) {
                        // with each playing room
                        JSONObject jRoom = new JSONObject();
                        jRoom.put("room_id", roomEntity.mRoomId);
                        jRoom.put("room_name", roomEntity.mRoomName);

                        jRoom.put("room_owner", roomEntity.mRoomOwnerName);



                        if (roomEntity.mPassword != null) {
                            jRoom.put("isSecure", true);
                            jRoom.put("password", roomEntity.mPassword);
                        } else {
                            jRoom.put("isSecure", false);
                        }
                        // attached object
                        switch (getWaitingList.zoneID) {
                            
                           
                            
                            default:
                                break;
                        }
                        jRoom.put("playing_size", roomEntity.mPlayingSize);
                        // then add to array of rooms

                        if (roomEntity.mPlayingSize > 0) {
                            arrRooms.put(jRoom);
                        }
                    }
                }
                encodingObj.put("waiting_rooms", arrRooms);
            }
            // response encoded obj
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
