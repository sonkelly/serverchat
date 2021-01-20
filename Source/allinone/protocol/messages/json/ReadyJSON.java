package allinone.protocol.messages.json;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.MessagesID;
import allinone.data.ResponseCode;
import allinone.protocol.messages.ReadyRequest;
import allinone.protocol.messages.ReadyResponse;

public class ReadyJSON implements IMessageProtocol {

	private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
			ReadyJSON.class);

	public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
			throws ServerException {
		try {
			
			// request data
			JSONObject jsonData = (JSONObject) aEncodedObj;
			// request messsage
			ReadyRequest matchReady = (ReadyRequest) aDecodingObj;
			
			// parsing
//            if(jsonData.has("v"))
//            {

				String v= jsonData.getString("v");
                String[] arrValues = v.split(AIOConstants.SEPERATOR_BYTE_1);
                matchReady.matchID = Long.parseLong(arrValues[0]);
                matchReady.ready = Integer.parseInt(arrValues[1])==1;
                
                return true;

//            }
//                        
//                        
//                        if(jsonData.has("match_id"))
//			    matchReady.matchID = jsonData.getLong("match_id");
//                            
//                        if(jsonData.has("uid"))
//			    matchReady.uid = jsonData.getLong("uid");
//			if (jsonData.has("ready")) {
//				matchReady.ready = jsonData.getBoolean("ready");
//			} else {
//				matchReady.ready = true;
//			}
                        

		} catch (Throwable t) {
			mLog.error("[DECODER] " + aDecodingObj.getID(), t);
			return false;
		}
	}

//	public void addPhomData(ReadyResponse matchJoin, JSONObject jCell,
//			PhomPlayer player) {
//		try {
//			jCell.put("id", player.id);
//			jCell.put("username", player.username);
//			jCell.put("level", player.level);
//			jCell.put("avatar", player.avatarID);
//			jCell.put("money", player.cash);
//			jCell.put("isReady", player.isReady);
//			jCell.put("isAuto", player.isAutoPlay);
//		} catch (Exception e) {
//		}
//
//	}
        
        private String getMidReady(ReadyResponse  matchReady)
        {
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(MessagesID.MATCH_READY)).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(matchReady.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            
            if(matchReady.mCode == ResponseCode.SUCCESS)
            {
            
                sb.append(Long.toString(matchReady.mUid)).append(AIOConstants.SEPERATOR_BYTE_1);
                if(matchReady.ready)
                {
                    sb.append("1");
                }
                else
                {
                    sb.append("0");
                }
                
//                sb.append(AIOConstants.SEPERATOR_BYTE_1).append(matchReady.startCount);
                
            }
            else
            {
                sb.append(matchReady.mErrorMsg);
            }
            
            return sb.toString();
                                
        }
        
        
	public Object encode(IResponseMessage aResponseMessage)
			throws ServerException {
		try {

			JSONObject encodingObj = new JSONObject();
            ReadyResponse matchReady = (ReadyResponse) aResponseMessage;
            encodingObj.put("v", getMidReady(matchReady));
            return encodingObj;

		} catch (Throwable t) {
			mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
			return null;
		}
	}
}
