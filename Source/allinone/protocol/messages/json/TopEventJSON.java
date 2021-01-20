package allinone.protocol.messages.json;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
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
import allinone.protocol.messages.TopEventResponse;

public class TopEventJSON implements IMessageProtocol {

    private final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(TopEventJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
            return true;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
           
            TopEventResponse topEventPlayers = (TopEventResponse) aResponseMessage;
            if(topEventPlayers.session != null && topEventPlayers.session.getByteProtocol()> AIOConstants.PROTOCOL_ADVERTISING)
            {
                StringBuilder sb = new StringBuilder();
                sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Integer.toString(topEventPlayers.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
                if (topEventPlayers.mCode == ResponseCode.FAILURE) {
                	 sb.append(topEventPlayers.mErrorMsg);
                }else {
                	sb.append(topEventPlayers.title).append(AIOConstants.SEPERATOR_BYTE_1);
                	sb.append(topEventPlayers.fromDate).append(AIOConstants.SEPERATOR_BYTE_1);
                	sb.append(topEventPlayers.toDate);//.append(AIOConstants.SEPERATOR_BYTE_3);
                	if (topEventPlayers.players != null) {
                		StringBuilder sb1 = new StringBuilder();
                		sb1.append(AIOConstants.SEPERATOR_BYTE_3);
                        for (int i = 0; i< topEventPlayers.players.size(); i++) {
                            UserEntity userEntity = topEventPlayers.players.get(i);
                            sb1.append(userEntity.mUsername).append(AIOConstants.SEPERATOR_BYTE_1);
                            sb1.append(userEntity.mUid).append(AIOConstants.SEPERATOR_BYTE_1);
                            sb1.append(userEntity.money).append(AIOConstants.SEPERATOR_BYTE_1);
                            sb1.append(userEntity.avatarID).append(AIOConstants.SEPERATOR_BYTE_2);
                        }
                        if(sb1.length()>0) sb1.deleteCharAt(sb1.length()-1);
                        sb.append(sb1);
                    }
                }
                encodingObj.put("v", sb.toString());
                return encodingObj;
            }
            
            encodingObj.put("mid", aResponseMessage.getID());
            encodingObj.put("code", topEventPlayers.mCode);
            if (topEventPlayers.mCode == ResponseCode.FAILURE) {
            } else if (topEventPlayers.mCode == ResponseCode.SUCCESS) {
                JSONArray arrRooms = new JSONArray();
                if (topEventPlayers.players != null) {
                    for (int i = 0; i< topEventPlayers.players.size(); i++) {
                        UserEntity userEntity = topEventPlayers.players.get(i);
						Map jRoom = new LinkedHashMap();
                        jRoom.put("name", userEntity.mUsername);
                        jRoom.put("uid", userEntity.mUid);
                        jRoom.put("money", userEntity.money);
                        jRoom.put("avatar", userEntity.avatarID);
                        arrRooms.put(jRoom);
                    }
                }
                encodingObj.put("tPlayers", arrRooms);
                encodingObj.put("fromDate", topEventPlayers.fromDate);
                encodingObj.put("toDate", topEventPlayers.toDate);
                encodingObj.put("title", topEventPlayers.title);
                
            }
            // response encoded obj
            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
