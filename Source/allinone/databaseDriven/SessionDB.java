/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.databaseDriven;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.slf4j.Logger;
import vn.game.common.LoggerContext;
import allinone.data.SessionEntity;

/**
 *
 * @author Zeple
 */
@SuppressWarnings("unused")
public class SessionDB {

    private static final Logger mLog = LoggerContext.getLoggerFactory()
            .getLogger(UserDB.class);

    public SessionDB() {
    }

    public SessionEntity getSession(long userID) throws Exception {
        Connection con = null;

       

        PreparedStatement stm = null;
        ResultSet rs = null;
        SessionEntity ss = new SessionEntity();
        try {
            con = DBGame.instance().getConnection();
            StringBuffer query = new StringBuffer();
            query.append("select * from sessions where uid =? limit 1");

            stm = con.prepareStatement(query.toString());
            stm.setLong(1, userID);
            rs = stm.executeQuery();
            if (rs.first()) {

                ss.uid = rs.getLong("uid");
                ss.timestamp = rs.getInt("timestamp");
                ss.name = rs.getString("name");
                ss.hostname = rs.getString("hostname");
                ss.session = rs.getString("session");
                ss.sid = rs.getString("sid");

            } else {//insert session

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
        return ss;
    }

    public ArrayList<SessionEntity> addSessionDB(long uid, String sid, String name, String hostname, String session) throws Exception {
        Connection con = null;

        //con = DBGame.instance().getConnection();

        PreparedStatement stm = null;
        PreparedStatement stmUpdate = null;
        PreparedStatement stmInsert = null;
        ResultSet rs = null;
        ArrayList result = new ArrayList();
        SessionEntity ss = new SessionEntity();
//        try {
//            long start = System.currentTimeMillis();
//            int timestamp = (int) (start / 1000L);
//
//            StringBuffer query = new StringBuffer();
//            query.append(" select * from sessions where uid =? limit 1");
//
//            stm = con.prepareStatement(query.toString());
//            stm.setLong(1, uid);
//
//            rs = stm.executeQuery();
//            if (rs.first()) {
//                ss.uid = rs.getLong("uid");
//                ss.sid = rs.getString("sid");
//                ss.timestamp = rs.getInt("timestamp");
//                ss.name = rs.getString("name");
//                ss.hostname = rs.getString("hostname");
//                ss.session = rs.getString("session");
//                if (!rs.getString("sid").equals(sid)) {//update session moi
//                    stmUpdate = con.prepareStatement("UPDATE sessions SET sid = ?, hostname = ?, timestamp =?, session = ? WHERE uid = ?", 1);
//                    stmUpdate.setString(1, sid);
//                    stmUpdate.setString(2, hostname);
//                    stmUpdate.setInt(3, timestamp);
//                    stmUpdate.setString(4, session);
//                    stmUpdate.setLong(5, uid);
//                    stmUpdate.executeUpdate();
//                    ss.sid = sid;
//                    ss.timestamp = timestamp;
//                    ss.name = name;
//                    ss.hostname = hostname;
//                    ss.session = session;
//                }
//
//                result.add(ss);
//            } else {//insert session
//
//                StringBuffer queryInsert = new StringBuffer();
//                queryInsert.append("INSERT INTO sessions (uid,name,sid,hostname,timestamp,session) ");
//                queryInsert.append("VALUES (? , ? , ? , ?, ?, ?)");
//
//                stmInsert = con.prepareStatement(queryInsert.toString(), 1);
//                stmInsert.setLong(1, uid);
//                stmInsert.setString(2, name);
//                stmInsert.setString(3, sid);
//                stmInsert.setString(4, hostname);
//                stmInsert.setInt(5, timestamp);
//                stmInsert.setString(6, session);
//                int i = stmInsert.executeUpdate();
//                if (i > 0) {
//                    mLog.debug("inssert getSession");
//                    ss.uid = uid;
//                    ss.sid = sid;
//                    ss.timestamp = timestamp;
//                    ss.name = name;
//                    ss.hostname = hostname;
//                    ss.session = session;
//                    result.add(ss);
//                }
//            }
//
//        } finally {
//            if (con != null) {
//                con.close();
//            }
//
//            if (stm != null) {
//                stm.close();
//            }
//            if (stmUpdate != null) {
//                stmUpdate.close();
//            }
//            if (stmInsert != null) {
//                stmInsert.close();
//            }
//            if (rs != null) {
//                rs.close();
//            }
//
//        }
        return result;
    }

    public void updateSessionDB(long uid, String sid, String name, String hostname, String session) throws Exception {
//        Connection con = null;
//
//        con = DBPoolConnectionGame.getConnection();
//
//        PreparedStatement stmUpdate = null;
//        try {
//            long start = System.currentTimeMillis();
//            int timestamp = (int) (start / 1000L);
//            stmUpdate = con.prepareStatement("UPDATE sessions SET sid = ?, hostname = ?, timestamp =?, session = ? WHERE uid = ?", 1);
//            stmUpdate.setString(1, sid);
//            stmUpdate.setString(2, hostname);
//            stmUpdate.setInt(3, timestamp);
//            stmUpdate.setString(4, session);
//            stmUpdate.setLong(5, uid);
//            stmUpdate.executeUpdate();
//
//        } finally {
//            if (con != null) {
//                con.close();
//            }
//
//            if (stmUpdate != null) {
//                stmUpdate.close();
//            }
//
//        }
    }

    public static void main(String[] args) {

//        try {
//            SessionEntity loge = new SessionEntity();
//            SessionDB logev = new SessionDB();
////            ArrayList<SessionEntity> loges = logev.addSessionDB(1, "33333tester2222222222222222", "253654645", "q533453645", "25326546");
////            for (SessionEntity ett : loges) {
////                System.out.println("log:" + ett.sid);
////            }
//            //get session
//            SessionEntity ett = logev.getSession(312855);
//            System.out.println("log:" + ett.sid);
//
//        } catch (Exception ex) {
//            java.util.logging.Logger.getLogger(LogvascDB.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }
}
