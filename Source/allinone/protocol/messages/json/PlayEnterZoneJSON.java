package allinone.protocol.messages.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import vn.game.room.Zone;
import allinone.data.AIOConstants;
import allinone.data.ResponseCode;
import allinone.data.SimpleTable;
import allinone.protocol.messages.PlayEnterZoneRequest;
import allinone.protocol.messages.PlayEnterZoneResponse;
import allinone.room.NRoomEntity;
import org.json.JSONException;
import vn.game.common.CommonUtils;

public class PlayEnterZoneJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(PlayEnterZoneJSON.class);

    @Override
    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            PlayEnterZoneRequest enterZone = (PlayEnterZoneRequest) aDecodingObj;

            String[] arr = jsonData.getString("v").split(AIOConstants.SEPERATOR_BYTE_1);
            if (!arr[0].equals("undefined")) {

                enterZone.zoneID = Integer.parseInt(arr[0]);
                enterZone.cacheVersion = Integer.parseInt(arr[1]);
//            enterZone.level = Integer.parseInt(arr[2]);
                return true;
            } else {
                return false;
            }

        } catch (NumberFormatException | JSONException t) {
            mLog.error("[DECODER] " + aDecodingObj.getID(), t);
            return false;
        }
    }

    private String getAllRoomInfo(PlayEnterZoneResponse response) {

        StringBuilder sb = new StringBuilder();
        List<SimpleTable> tables;

        if (response.lstRooms != null) {

            int roomSize = response.lstRooms.size();
            int ZoneId = response.session.getCurrentZone();
            Zone zone = response.session.findZone(ZoneId);

//                mLog.debug(" roomSize " + roomSize);
//                mLog.debug(" zone " + ZoneId);
            int lastLevel = 0;

            for (int i = 0; i < roomSize; i++) {
                NRoomEntity entity = response.lstRooms.get(i);

                int playing;

                int tableSize = 0;
                tables = new ArrayList<>();

                if (entity.getPhong() == null) {
                    playing = 0;
                } else {
//                    mLog.debug(" Phong Id " + entity.getPhong().id);
//                    mLog.debug(" level " + entity.getPhong().level);
//                        playing = entity.getPhong().getPlaying();
                    tables = zone.dumpAllTablesOfPhong(entity.getPhong().id);
//                        tables = zone.dumpAllTablesOfPhongTest(entity.getPhong().id);                        
                    tableSize = tables.size();

                }

//                    mLog.debug(" playing " + playing);
                //mLog.debug(" tableSize " + tableSize);
                StringBuilder dataTables = new StringBuilder();
                String tableIndex = "";

//                    ArrayList<Integer> moneys = new ArrayList<Integer>();
//                    
//                    moneys.add(new Integer(100));
//                    moneys.add(new Integer(200));
//                    moneys.add(new Integer(500));
//                    moneys.add(new Integer(1000));
//                    moneys.add(new Integer(2000));
//                    moneys.add(new Integer(5000));
//                    moneys.add(new Integer(10000));
//                    moneys.add(new Integer(20000));
//                    moneys.add(new Integer(50000));
//                    for (int j = 0; j < 10; j++) {
////                        SimpleTable table = tables.get(j);
////                        if (table != null) {
////                        	tableIndex = entity.getPhong().id + "" + table.getTableIndex();
//                    		int index = i* 10 + j;
//                        	tableIndex = String.valueOf(index);
//                        	dataTables.append(index).append(AIOConstants.SEPERATOR_BYTE_1);
//                        	dataTables.append(new Random().nextInt(4) + 1).append(AIOConstants.SEPERATOR_BYTE_1);
//                        	int money = new Random().nextInt(9);
//                        	dataTables.append(moneys.get(money)).append(AIOConstants.SEPERATOR_BYTE_1);
//                        	dataTables.append(index).append(AIOConstants.SEPERATOR_BYTE_1);                        	
//                        	dataTables.append(4).append(AIOConstants.SEPERATOR_BYTE_1);
//                        	dataTables.append("").append(AIOConstants.SEPERATOR_BYTE_1);
//                        	dataTables.append(1).append(AIOConstants.SEPERATOR_BYTE_2);
////                        }                        
//                    }
                for (int j = 0; j < tableSize; j++) {
                    SimpleTable table = tables.get(j);
                    if (table != null) {
                        //mLog.debug("entity.getPhong().id:" + entity.getPhong().id);
//                        	tableIndex = entity.getPhong().id + "" + table.getTableIndex();                        	
                        tableIndex = String.valueOf(table.getTableIndex());
                        dataTables.append(tableIndex).append(AIOConstants.SEPERATOR_BYTE_1);
                        dataTables.append(table.getTableSize()).append(AIOConstants.SEPERATOR_BYTE_1);
                        dataTables.append(table.firstCashBet).append(AIOConstants.SEPERATOR_BYTE_1);
                        dataTables.append(table.matchID).append(AIOConstants.SEPERATOR_BYTE_1);
                        dataTables.append(table.maximumPlayer).append(AIOConstants.SEPERATOR_BYTE_1);
                        dataTables.append(table.name).append(AIOConstants.SEPERATOR_BYTE_1);
                        dataTables.append(table.isPlaying ? "1" : "0").append(AIOConstants.SEPERATOR_BYTE_1);
                        dataTables.append(entity.getPhong().id).append(AIOConstants.SEPERATOR_BYTE_2);
                    }
                }

                if (dataTables.length() > 0) {
                    dataTables.deleteCharAt(dataTables.length() - 1);
                }

                if (lastLevel != entity.getLv()) {
//                        sb.append(entity.getId()).append(AIOConstants.SEPERATOR_BYTE_1);
//                        sb.append(playing).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(entity.getLv()).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(entity.getMinCash()).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(entity.getPhong().id).append(AIOConstants.SEPERATOR_BYTE_1);
                    sb.append(CommonUtils.checkAccessJoinRoom(entity.getMinCash(), response.session.getUserEntity().level)).append(AIOConstants.SEPERATOR_BYTE_3);

                    lastLevel = entity.getLv();
                }

                if (dataTables.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                    sb.append(AIOConstants.SEPERATOR_BYTE_2);
                    sb.append(dataTables);
                    sb.append(AIOConstants.SEPERATOR_BYTE_3);
                }

            }

            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            } else {
                sb.append(""); // danh sach rong
            }

            return sb.toString();
        }

        return sb.toString();

    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            PlayEnterZoneResponse enterZone = (PlayEnterZoneResponse) aResponseMessage;
            StringBuilder valueSb = new StringBuilder();
            valueSb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
            valueSb.append(Integer.toString(enterZone.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);

            if (enterZone.mCode == ResponseCode.FAILURE) {
                valueSb.append(enterZone.mErrorMsg);
            } else {
                valueSb.append(this.getAllRoomInfo((PlayEnterZoneResponse) aResponseMessage));
                valueSb.append(AIOConstants.SEPERATOR_BYTE_2).append(enterZone.session.getCurrentZone());
            }

            encodingObj.put("v", valueSb.toString());

            return encodingObj;
        } catch (Throwable t) {
            mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
            return null;
        }
    }

}
