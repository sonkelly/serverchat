/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.databaseDriven;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import allinone.data.AIOConstants;
import allinone.data.ZoneID;
import allinone.room.NRoomEntity;
import java.sql.PreparedStatement;

/**
 *
 * @author Administrator
 */
public class RoomDB {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(DBCache.class);
    static Map<Integer, List<NRoomEntity>> lstRooms = new HashMap<>();
    private static final String PLAYING_PARAM = "playing";
    private static final String ROOM_ID_PARAM = "roomId";
    private static Map<Integer, String> lstGameCache = new HashMap<>();

    public RoomDB() {
    }

    public static void reload() {
        mLog.info("[WF] ---- 4.3.1 reload()");
        try {
            initAllRooms();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static String getGameInfo(int zoneId) {
        return lstGameCache.get(zoneId);
    }

    private static void initAllRooms() throws SQLException, IllegalArgumentException, IllegalAccessException {
        StringBuilder query = new StringBuilder();
        Connection con = null;

        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBVip.instance().getConnection();
            query.append("select * from room INNER JOIN roomlevel on room.levelId=roomlevel.id");
            stm = con.prepareStatement(query.toString());

            rs = stm.executeQuery();

            List<NRoomEntity> res = new ArrayList<>();

            while (rs.next()) {

                NRoomEntity entity = new NRoomEntity(rs.getInt("id"), rs.getInt("playing"), rs.getInt("availables"), rs.getInt("numTable"), rs.getString("level"), rs.getInt("number"),
                        rs.getInt("levelId"), rs.getInt("minCash"), rs.getInt("zoneId"), rs.getInt("typeMoney"));

                res.add(entity);
            }

            // res.addAll(TopicManager.rooms()); // add newRoom for New_PIKA
            Field[] zonesId = ZoneID.class.getFields();
            for (Field f : zonesId) {
                int zoneID = (Integer) f.get(new ZoneID());
                StringBuilder sb = new StringBuilder();
                List<NRoomEntity> rooms = new ArrayList<>();
                int size = res.size();
                boolean flagAddZone = false;
                for (int j = 0; j < size; j++) {
                    if (res.get(j).getZoneId() == zoneID) {
                        flagAddZone = true;
                        NRoomEntity entity = res.get(j);
                        rooms.add(res.get(j));
                        sb.append(entity.getId()).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(entity.getLv()).append(AIOConstants.SEPERATOR_BYTE_2);
                    }
                }

                if (flagAddZone) {
                    sb.deleteCharAt(sb.length() - 1);
                }

                lstGameCache.put(zoneID, sb.toString());
                lstRooms.put(zoneID, rooms);
            }

        } finally {

            if (con != null) {
                con.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    public static List<Long> getAllMoney() throws SQLException, IllegalArgumentException, IllegalAccessException {
        StringBuilder query = new StringBuilder();
        Connection con = null;

        PreparedStatement stm = null;
        ResultSet rs = null;

        List<Long> res = new ArrayList<>();
        try {
            con = DBVip.instance().getConnection();
            query.append("select DISTINCT minCash from room INNER JOIN roomlevel on room.levelId=roomlevel.id");
            stm = con.prepareStatement(query.toString());

            rs = stm.executeQuery();

            while (rs.next()) {

                res.add(rs.getLong("minCash"));
            }

        } catch (SQLException e) {
            mLog.error(e.getMessage());
        } finally {

            if (con != null) {
                con.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return res;

    }

//    public List<NRoomEntity> getRooms(int zoneId) {
//        return lstRooms.get(zoneId);
//
//    }
    public List<NRoomEntity> getRooms(int zoneId, String realmoney) {
        List<NRoomEntity> list = lstRooms.get(zoneId);
        List<NRoomEntity> newListRealNoney = new ArrayList<NRoomEntity>();
        List<NRoomEntity> newList = new ArrayList<NRoomEntity>();
        //mLog.debug("list:"+list.size());
        for (int i = 0; i < list.size(); i++) {
            try {

                NRoomEntity roomEntity = list.get(i);
                //mLog.debug("roomEntity.getMoneyType():"+roomEntity.getMoneyType());
                if (roomEntity.getMoneyType() == 1) {//realmoney

                    newListRealNoney.add(roomEntity);
                } else {
                    newList.add(roomEntity);
                }
            } catch (Exception e) {
                mLog.debug("error" + e.getMessage());
            }
        }
        if (realmoney.equals(AIOConstants.PRIFIX_REALMONEY)) {
            return newListRealNoney;
        } else {
            return newList;
        }

    }

    public NRoomEntity getRoomEntity(int zoneId, int phongId, String realmoney) {
        NRoomEntity entity = null;
        List<NRoomEntity> rooms = getRooms(zoneId, realmoney);
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getId() == phongId || rooms.get(i).getZoneId() == ZoneID.XOC_DIA_ALL) {
                entity = rooms.get(i);
                break;
            }
        }
        return entity;
    }

    public int validateMoneySetting(int roomId, int money, long userId) throws SQLException {
        int res;

        String query = "{?= call uspValidateChangeSetting(?,?,?) }";
        Connection conn = DBPoolConnection.getConnection();
        try {
            CallableStatement cs = conn.prepareCall(query);
            cs.registerOutParameter(1, Types.INTEGER);

            cs.setInt(2, roomId);
            cs.setInt(3, money);
            cs.setLong(4, userId);

            cs.execute();
            res = cs.getInt(1);

            cs.close();
        } finally {
            conn.close();
        }

        return res;

    }

    public void setPhongStatus(int roomId, int playing) throws SQLException {
        String query = "{ call uspSetPhongStatus(?,?) }";
        Connection con = DBPoolConnection.getConnection();
        try {
            CallableStatement cs = con.prepareCall(query);
            cs.setInt(ROOM_ID_PARAM, roomId);
            cs.setInt(PLAYING_PARAM, playing);

            cs.execute();
            cs.clearParameters();
            cs.close();

        } finally {
            con.close();
        }

    }
}
