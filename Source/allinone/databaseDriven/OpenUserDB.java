/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.databaseDriven;

import allinone.data.OpenUserEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import vn.game.common.LoggerContext;

/**
 *
 * @author Zeple
 */
public class OpenUserDB {

    private Connection conn;
    private Connection connGame;

    public OpenUserDB() {
    }

//    public OpenUserDB(Connection con) {
//        this.connGame = con;
//    }
    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(OpenUserDB.class);
    public OpenUserEntity getOpenUser(String open_id) {

        long start = System.currentTimeMillis();

        OpenUserEntity usopen = null;

        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {

            //conn = getConnection();
            conn = DBGame.instance().getConnection();

            stm = conn.prepareStatement("SELECT id,uid,email,link,access_token FROM open_user WHERE open_id=?");
            stm.setString(1, open_id);

            rs = stm.executeQuery();

            if (rs.first()) {

                usopen = new OpenUserEntity(rs.getInt("id"), open_id, rs.getInt("uid"), rs.getString("email"), rs.getString("link"), 1, rs.getString("access_token"));

                long count = System.currentTimeMillis() - start;
                if (count > 100L) {

                }

                return usopen;
            } else {
                return null;
            }

        } catch (SQLException e) {

        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
                if (this.connGame != null) {
                    this.connGame.close();
                }
            } catch (SQLException sqle) {
                mLog.debug("getOpenUser error:"+sqle.toString());
            }
        }
        return null;
    }

    //insert bang open id
    public int insertOpenUser(String open_id, int uid, String email, int type, String link, String access_token) {
        long start = System.currentTimeMillis();

        int generatedID = -1;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = DBGame.instance().getConnection();
            stm = conn.prepareStatement("INSERT INTO open_user (open_id, email, type, link, access_token,uid,created) VALUES (?,?,?,?,?,?,?)", 1);
            stm.setString(1, open_id);
            stm.setString(2, email);
            stm.setInt(3, type);
            stm.setString(4, link);
            stm.setString(5, access_token);
            stm.setInt(6, uid);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String now = dateFormat.format(date);
            stm.setString(7, now);

            stm.executeUpdate();
            long count = System.currentTimeMillis() - start;
            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                generatedID = rs.getInt(1);

            }
            if (count > 100L) {

            }

            return generatedID;
        } catch (SQLException e) {

        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                mLog.debug("insertOpenUser error:"+sqle.toString());
            }
        }
        return 0;
    }
}
