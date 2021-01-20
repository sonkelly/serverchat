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
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

import allinone.data.AIOConstants;
import allinone.data.UserEntity;
import allinone.data.UsersLoginDayEntity;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import vn.game.session.ISession;

/**
 *
 * @author Zeple
 */
public class UsersLoginDayDB {

    private static final long DAY_BONUS = 1000;
    private static final long DAY_BONUS_LEVEL = 0;
    private static final long DAY_BONUS_FRIEND = 0;

    public static UsersLoginDayEntity getTotalDay(long uid) {
        UsersLoginDayEntity user = null;
        Connection con = null;

        PreparedStatement stm = null;
        ResultSet rs = null;
        int total = 0;
        try {
            con = DBGame.instance().getConnection();
            String query = "select * from users_login_day where uid = ?";
            stm = con.prepareStatement(query);
            stm.setLong(1, uid);
            rs = stm.executeQuery();

            if (rs.first()) {
                total = rs.getInt("countDay");
                user = new UsersLoginDayEntity();
                user.uid = rs.getLong("uid");
                user.viewname = rs.getString("viewname");
                user.startDay = rs.getTimestamp("startDay");
                user.curLoginDay = rs.getTimestamp("curLoginDay");
                user.moneyDay = rs.getLong("moneyDay");
                user.moneyLevel = rs.getLong("moneyLevel");
                user.moneyFriend = rs.getLong("moneyFriend");
                user.moneyTotal = rs.getLong("moneyTotal");
                user.countDay = rs.getInt("countDay");
            }

        } catch (SQLException e1) {

        } finally {

            try {
                if (stm != null) {
                    stm.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {

            }
        }
        return user;
    }

    public static long addUserLoginDay(long uid, String viewname) throws Exception {

        String query = "INSERT INTO users_login_day (uid, viewname, startDay,curLoginDay,moneyDay,moneyLevel,moneyFriend,moneyTotal,countDay) VALUES (? , ? , ?, ?, ? , ? , ? , ?, ? )";

        Connection con = null;

        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBGame.instance().getConnection();
            stm = con.prepareStatement(query, 1);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String now = dateFormat.format(date);

            stm.setLong(1, uid);
            stm.setString(2, viewname);
            stm.setString(3, now);
            stm.setString(4, now);
            stm.setLong(5, UsersLoginDayDB.DAY_BONUS);
            stm.setLong(6, UsersLoginDayDB.DAY_BONUS_LEVEL);
            stm.setLong(7, UsersLoginDayDB.DAY_BONUS_FRIEND);
            stm.setLong(8, (UsersLoginDayDB.DAY_BONUS + UsersLoginDayDB.DAY_BONUS_FRIEND + UsersLoginDayDB.DAY_BONUS_LEVEL));
            stm.setLong(9, 1);
            stm.executeUpdate();

            rs = stm.getGeneratedKeys();
            long lid = 0;
            if (rs.next()) {
                lid = rs.getLong(1);
            }
            return lid;

        } finally {
            con.close();
        }
    }

    public static long processLoginDay(UserEntity entity) {
        long uid = entity.mUid;
        UsersLoginDayEntity user = UsersLoginDayDB.getTotalDay(uid);
        long curMoney = 0;
        if (user == null) {
            try {
                UsersLoginDayDB.addUserLoginDay(uid, entity.viewName);

            } catch (Exception ex) {
                Logger.getLogger(UsersLoginDayDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //update day
            try {
                UsersLoginDayDB.updateUserLoginDay(user);

            } catch (Exception ex) {
                Logger.getLogger(UsersLoginDayDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return curMoney;

    }

    public static void updateUserLoginDay(UsersLoginDayEntity user) throws Exception {

        StringBuffer query = new StringBuffer();

        Connection con = null;

        PreparedStatement stm = null;
        ResultSet rs = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String now = dateFormat.format(date);

        Date date1 = user.curLoginDay;
        Date date2 = dateFormat.parse(now);
        long diff = date2.getTime() - date1.getTime();
        long DayLogin = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        //System.out.println("Days: " + DayLogin);
        if (DayLogin > 1) {
            query.append("update users_login_day set startDay = ?, curLoginDay = ?, countDay = 1 ");
        } else if (DayLogin <= 1) {
            query.append("update users_login_day set curLoginDay =?, countDay = countDay + 1 ");
        }

        query.append("WHERE uid = ?");

        try {
            con = DBGame.instance().getConnection();
            stm = con.prepareStatement(query.toString());
            if (DayLogin > 1) {
                stm.setString(1, now);
                stm.setString(2, now);
                stm.setLong(3, user.uid);
                stm.executeUpdate();
            } else if (DayLogin <= 1) {
                stm.setString(1, now);
                stm.setLong(2, user.uid);
                stm.executeUpdate();
            }

            

        } finally {
            con.close();
        }
    }
}
