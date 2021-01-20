/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.databaseDriven;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import allinone.data.UserInfoEntity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author mac
 */
public class UserInfoDB {

    private Connection conn;
    private final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(FriendDB.class);

    public UserInfoDB() {
    }

    public UserInfoDB(Connection con) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public UserInfoEntity getUserInfo(long userId) throws SQLException {
        UserInfoEntity res = null;
        String query = "SELECT * from users_info WHERE uid = ?";
        Connection con = DBGame.instance().getConnection();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {

            stm = con.prepareStatement(query);
            stm.setLong(1, userId);

            rs = stm.executeQuery();
            if (rs.next()) {

                res = new UserInfoEntity();
                res.countryId = rs.getString("country");
                res.cityId = rs.getString("city");
                res.address = rs.getString("address");
                res.cueId = rs.getInt("cueId");
                res.soccerStarFormat = rs.getInt("soccerStarFormat");
                res.soccerStarStyle = rs.getInt("soccerStarStyle");

            }

        } finally {
            if (con != null) {
                con.close();
            }

            if (rs != null) {
                rs.close();
            }

            if (stm != null) {
                stm.close();
            }
        }
        return res;
    }

    public long insert(long uid, String country) throws Exception {
        if (country == null || country.equals("")) {
            country = "vn";//mac dinh viet nam
        }
        Connection con = null;

        PreparedStatement stm = null;
        ResultSet rs = null;
        long id = 0;
        try {
            //get userinfo 
            UserInfoEntity userInfoObj = this.getUserInfo(uid);
            if (userInfoObj == null) {
                con = DBGame.instance().getConnection();

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String now = dateFormat.format(date);

                StringBuffer query = new StringBuffer();
                query.append("INSERT INTO users_info (uid, country,cueId, createdtime) ");
                query.append("VALUES (? , ?, ? ,?)");
                stm = con.prepareStatement(query.toString(), 1);

                stm.setLong(1, uid);
                stm.setString(2, country);
                stm.setInt(3, 1);
                stm.setString(4, now);
                stm.executeUpdate();
                rs = stm.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getInt(1);

                    //end log count request friend
                    return id;//ok

                }
            }

        } catch (Exception e) {
            mLog.debug("error:" + e.getMessage());
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
        return id;
    }

    public long updateForSoccerStar(long uid, int soccerStarFormat, int soccerStarStyle) throws Exception {

        Connection con = null;

        PreparedStatement stm = null;
        ResultSet rs = null;
        long id = 0;
        try {
            //get userinfo 
            UserInfoEntity userInfoObj = this.getUserInfo(uid);
            if (userInfoObj != null) {
                con = DBGame.instance().getConnection();

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String now = dateFormat.format(date);

                StringBuffer query = new StringBuffer();
                query.append("UPDATE users_info SET soccerStarFormat =?,soccerStarStyle = ? ,modifiedDate =? WHERE uid = ? ");

                stm = con.prepareStatement(query.toString(), 1);

                stm.setInt(1, soccerStarFormat);
                stm.setInt(2, soccerStarStyle);
                stm.setString(3, now);
                stm.setLong(4, uid);
                stm.executeUpdate();
                rs = stm.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getInt(1);

                    //end log count request friend
                    return id;//ok

                }
            }

        } catch (Exception e) {
            mLog.debug("error:" + e.getMessage());
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
        return id;
    }

    public static void main(String[] args) throws Exception {
        UserInfoDB obj = new UserInfoDB();
        //obj.insert(1, "vn");
        //obj.getUserInfo(103);
//        obj.insert(2, "vn");
//        obj.updateForSoccerStar(244140, "[\"1\",\"5\",\"3\"]", 2);

    }
}
