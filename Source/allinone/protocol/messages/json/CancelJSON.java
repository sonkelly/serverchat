/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages.json;

//import java.util.List;
import org.json.JSONObject;
import org.slf4j.Logger;


//import allinone.room.NRoomEntity;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
//import allinone.data.SimpleTable;
//import allinone.data.ZoneID;
//import allinone.databaseDriven.RoomDB;import allinone.data.AIOConstants;
import allinone.data.ResponseCode;
import allinone.protocol.messages.CancelRequest;
import allinone.protocol.messages.CancelResponse;

public class CancelJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(CancelJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        
    	try {
            
        	JSONObject jsonData = (JSONObject) aEncodedObj;
            CancelRequest matchCancel = (CancelRequest) aDecodingObj;
            
//            if(jsonData.has("v"))
//            {            
            
            matchCancel.mMatchId = Long.parseLong(jsonData.getString("v"));
            
//            return true;            
//            }
            
//            matchCancel.mMatchId = jsonData.getLong("match_id");
//            matchCancel.uid = jsonData.getLong("uid");
//            try {
//                matchCancel.isLogout = jsonData.getBoolean("is_logout");
//            } catch (Exception e) {
//                matchCancel.isLogout = false;
//            }
//            try {
//                matchCancel.roomID = jsonData.getInt("room_id");
//            } catch (Exception e) {
//                
//            }
//            try {
//                matchCancel.isOutOfGame = jsonData.getBoolean("is_out_game");
//            } catch (Exception e) {
//                matchCancel.isOutOfGame = false;
//            }
            
            return true;
            
        } catch (Throwable t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }
    
//    public void getPhong(int zoneId, StringBuilder sb)
//    {
//        try
//        {
//            RoomDB db = new RoomDB();
//            List<NRoomEntity> rooms = db.getRooms(zoneId);
//
//            int roomSize = rooms.size();
//    //                                    byte[] roomBytes = new byte[roomSize * 5];
//            for(int i = 0; i< roomSize; i++)
//            {
//                NRoomEntity entity = rooms.get(i);
//                int playing;
//                if(entity.getPhong() == null)
//                {
//                    playing = 0;
//                }
//                else
//                {
//                    playing = entity.getPhong().getPlaying();
//                }
//
//                sb.append(entity.getId()).append(AIOConstants.SEPERATOR_BYTE_1);
//                sb.append(playing).append(AIOConstants.SEPERATOR_BYTE_1);
//                sb.append(entity.getLv()).append(AIOConstants.SEPERATOR_BYTE_2);                    
//            }
//            
//            if(roomSize>0)
//            {
//                sb.deleteCharAt(sb.length() -1);
//            }
//            
//            sb.append(AIOConstants.SEPERATOR_BYTE_3);
//        }
//        catch(Exception ex)
//        {
//            mLog.error(ex.getMessage(), ex);
//        }
//    }
    
    private void getMobileInfo(CancelResponse matchCancel, StringBuilder sb, String sepEl, String sepArr, String sepDif)
    {
    	
        sb.append(Long.toString(matchCancel.uid)).append(sepEl);
        sb.append(Long.toString(matchCancel.newOwner)).append(sepEl);
        sb.append(Long.toString(matchCancel.cancelStatus));
        
//        if(matchCancel.zone != null)
//        {
//            List<SimpleTable> lstTables = matchCancel.zone.dumpNewWaitingTables(matchCancel.phongId);
//            
//            if(lstTables != null)
//            {
//                int tableSize = lstTables.size();
//                
//                for(int i = 0; i< tableSize; i++)
//                {
//                    SimpleTable table = lstTables.get(i);
//                    if(table != null)
//                    {
//                        sb.append(table.getTableIndex()).append(sepEl);
//                        sb.append(table.getTableSize()).append(sepEl);
//                        sb.append(table.firstCashBet).append(sepEl);
//                        sb.append(table.matchID).append(sepEl);
//                        sb.append(table.maximumPlayer).append(sepArr);
//                    }
//                }
//                if(tableSize>0)
//                {
//                    sb.deleteCharAt(sb.length()-1);
//                }
//            }
//        }
        
//        sb.append(sepDif);
//        
//        if (matchCancel.zone_id == ZoneID.TIENLEN) {
//            if (matchCancel.next_id != -1) {
//                sb.append(Long.toString(matchCancel.next_id)).append(sepEl);
//                sb.append(matchCancel.isNewRound?"1":"0");
//            }
//        }
    }
    
    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            CancelResponse matchCancel = (CancelResponse) aResponseMessage;
            
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            sb.append(Integer.toString(matchCancel.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
            
            if (matchCancel.mCode == ResponseCode.FAILURE) {
            	sb.append(matchCancel.mErrorMsg);
            }else {
                getMobileInfo(matchCancel, sb, AIOConstants.SEPERATOR_BYTE_1, AIOConstants.SEPERATOR_BYTE_2, 
                            AIOConstants.SEPERATOR_BYTE_3);
            }
            
            encodingObj.put("v", sb.toString());
            return encodingObj;
            
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }
}
