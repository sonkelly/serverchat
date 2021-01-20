package allinone.databaseDriven;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import tools.CacheEventInfo;
import vn.game.common.LoggerContext;
import allinone.data.AIOConstants;
import allinone.data.EventBonusEntity;
import allinone.data.EventEntity;
import allinone.data.EventPlayerEntity;
import allinone.data.GoldenHourEntity;
import allinone.data.UserEntity;
import java.util.logging.Level;

public class EventDB {

    private static final Logger mLog = LoggerContext.getLoggerFactory()
            .getLogger(EventDB.class);
    private static final String EVENT_ID_PARAM = "eventId";
//    private static List<EventEntity> lstEvents;
    public static final int EVENT_BONUS_TYPE_FACESHARE = 1;
    public static final int EVENT_BONUS_TYPE_BAONETVOTE = 2;
    public static final int EVENT_BONUS_TYPE_APPLINK = 3;
    public static final int EVENT_BONUS_TYPE_INAPP = 4;

//    public static void reload() {
//        try {
//            lstEvents = getEventFromDB();
//        } catch (SQLException ex) {
//            mLog.error(ex.getMessage(), ex);
//        }
//    }
    

    public static List<EventEntity> getEvents(int partnerId) {
        List<EventEntity> lstEvents = new CacheEventInfo().getEventList();
        int size = lstEvents.size();

        List<EventEntity> allPartners = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            EventEntity entity = lstEvents.get(i);
            if (entity.getPartnerId() == 0 || entity.getPartnerId() == partnerId) {
                allPartners.add(entity);
            }
        }

        List<EventEntity> retEvents;
        retEvents = allPartners;
        return retEvents;
    }

    public static String getAchivements(int partnerId) {
        List<EventEntity> lstEvents = new CacheEventInfo().getEventList();
        int size = lstEvents.size();
        List<EventEntity> allAchievement = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            EventEntity entity = lstEvents.get(i);
            if (entity.getPartnerId() == 0 || entity.getPartnerId() == partnerId) {
                allAchievement.add(entity);
            }
        }

        List<EventEntity> retEvents;
//        if (onlyPartner) {V
//            retEvents = achievePartners;
//        } else {
        retEvents = allAchievement;
        //}

        int retSize = retEvents.size();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < retSize; i++) {
            EventEntity achiveEntity = retEvents.get(i);
            if (achiveEntity.getGameId() >= 0) {
                sb.append(achiveEntity.getEventId()).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(achiveEntity.getTittle()).append(AIOConstants.SEPERATOR_BYTE_2);
            }
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public List<EventPlayerEntity> getTopPlayer(int eventId) throws SQLException {
        List<EventPlayerEntity> res = new ArrayList<EventPlayerEntity>();
        String query = "{ call uspGetTopEventPlayer(?) }";
        Connection conn = DBPoolConnection.getConnection();
        try {

            CallableStatement cs = conn.prepareCall(query);
            cs.clearParameters();
            cs.setInt(EVENT_ID_PARAM, eventId);
            ResultSet rs = cs.executeQuery();
            if (rs != null) {

                while (rs.next()) {

                    UserEntity user = new UserEntity();

                    user.mUid = rs.getLong("UserID");
                    //System.out.println("======================BinhLT:" + res.mUid);
                    user.mUsername = rs.getString("Name");

//                            user.money = rs.getLong("money");
                    user.avFileId = rs.getLong("avatarFileId");

                    String description = rs.getString("description");

                    EventPlayerEntity eventEntity = new EventPlayerEntity(user, description);
                    eventEntity.point = rs.getLong("point");
                    res.add(eventEntity);

                }

                rs.close();
            }
        } finally {
            conn.close();
        }
        return res;
    }

    public EventBonusEntity getEventBonusEntityByType(int type) throws SQLException {
        EventBonusEntity res = null;
        String query = "SELECT eventId, eventType, bonus, fromDate, toDate, imageLink, actionLink, description, eventKey "
                + " FROM eventdata "
                + " WHERE fromDate <= GETDATE() AND toDate >= GETDATE() AND eventType = " + type
                + " ORDER BY eventId ASC;";
        Connection conn = DBPoolConnection.getConnection();
        try {
            CallableStatement cs = conn.prepareCall(query);
            ResultSet rs = cs.executeQuery();
            if (rs != null) {
                if (rs.next()) {
                    res = new EventBonusEntity();
                    res.setId(rs.getInt("eventId"));
                    res.setType(rs.getInt("eventType"));
                    res.setFromDate(rs.getString("fromDate"));
                    res.setToDate(rs.getString("toDate"));
                    res.setBonus(rs.getInt("bonus"));
                    res.setImageLink(rs.getString("imageLink"));
                    res.setActionLink(rs.getString("actionLink"));
                    res.setDescription(rs.getString("description"));
                    res.setEventKey(rs.getString("eventKey"));
                }
                rs.close();
                cs.close();
            }
        } finally {
            conn.close();
        }
        return res;
    }

    public EventBonusEntity getEventBonusEntity() throws SQLException {
        EventBonusEntity res = null;
        String query = "SELECT eventId, eventType, bonus, fromDate, toDate, imageLink, actionLink, description, eventKey "
                + " FROM eventdata "
                + " WHERE isActive = 1 AND fromDate <= GETDATE() AND toDate >= GETDATE()"
                + " ORDER BY eventId ASC;";
        Connection conn = DBPoolConnection.getConnection();
        try {
            CallableStatement cs = conn.prepareCall(query);
            ResultSet rs = cs.executeQuery();
            if (rs != null) {
                if (rs.next()) {
                    res = new EventBonusEntity();
                    res.setId(rs.getInt("eventId"));
                    res.setType(rs.getInt("eventType"));
                    res.setFromDate(rs.getString("fromDate"));
                    res.setToDate(rs.getString("toDate"));
                    res.setBonus(rs.getInt("bonus"));
                    res.setImageLink(rs.getString("imageLink"));
                    res.setActionLink(rs.getString("actionLink"));
                    res.setDescription(rs.getString("description"));
                    res.setEventKey(rs.getString("eventKey"));
                }
                rs.close();
                cs.close();
            }
        } finally {
            conn.close();
        }
        return res;
    }

    public List<GoldenHourEntity> getGoldenHour() throws SQLException {
        List<GoldenHourEntity> res = new ArrayList<GoldenHourEntity>();
        String query = "SELECT id, fromDate, toDate, type, bonusAmount, partnerId, externalParam, description "
                + " FROM GoldenHour "
                + " WHERE isActive = 1 AND fromDate <= GETDATE() AND toDate >= GETDATE()"
                + " ORDER BY id ASC;";
        Connection conn = DBPoolConnection.getConnection();
        try {
            CallableStatement cs = conn.prepareCall(query);
            ResultSet rs = cs.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    GoldenHourEntity entity = new GoldenHourEntity();
                    entity.setId(rs.getInt("id"));
                    entity.setFromDate(rs.getString("fromDate"));
                    entity.setToDate(rs.getString("toDate"));
                    entity.setType(rs.getInt("type"));
                    entity.setBonusAmount(rs.getInt("bonusAmount"));
                    entity.setPartnerId(rs.getInt("partnerId"));
                    entity.setExtenalParam(rs.getString("externalParam"));
                    entity.setDescription(rs.getString("description"));
                    res.add(entity);
                }
                rs.close();
                cs.close();
            }
        } finally {
            conn.close();
        }
        return res;
    }

    public static List<EventEntity> getEventFromDB() throws SQLException {
        List<EventEntity> res = new ArrayList<EventEntity>();
        //String query = "{ call uspGetEvent() }";
        String query = "SELECT * FROM `events` where status =1 and gameID = 2";
        Connection conn = DBPoolConnectionAdmin.getConnection();
        CallableStatement cs = null;
        ResultSet rs = null;
        try {

             cs = conn.prepareCall(query);

             rs = cs.executeQuery();
            if (rs != null) {

                while (rs.next()) {
                    String title = rs.getString("description");
                    String content = rs.getString("content");
                    int eventId = rs.getInt("evid");
                    int partnerId = 0;
                    int gameId = rs.getInt("gameID");
                    boolean isConcurrent = rs.getBoolean("status");

                    EventEntity entity = new EventEntity(title, content, null, eventId, gameId);
                    entity.setPartnerId(partnerId);
                    entity.setConcurrent(isConcurrent);
                    res.add(entity);
                }

                //rs.close();
            }
        } finally {
            conn.close();
            if(cs != null){
                cs.close();
            }
            if(rs != null){
                rs.close();
            }
        }
        return res;
    }
}
