/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.databaseDriven;

import allinone.data.AIOConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Date;


import org.slf4j.Logger;

import vn.game.common.LoggerContext;

import allinone.data.LogvascEntity;

import allinone.data.UserEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;


/**
 *
 * @author Zeple
 */
@SuppressWarnings("unused")
public class LogvascDB {

    private static final Logger mLog = LoggerContext.getLoggerFactory()
            .getLogger(UserDB.class);
    private Connection conn;
    public static int LOG_TYPE_USER_CK_DAILY = 1010;//chuyentien
    public static int LOG_TYPE_USER_NHAN_DAILY = 1011;
    public static int LOG_TYPE_DAILY_NHAN_USER = 1020;
    public static int LOG_TYPE_DAILY_CK_USER = 1021;
    //tyle log
    public static int LOG_TYPE_MINIPOKER = 501;
    public static int LOG_TYPE_THAMGIA_EVENT = 502;
    ///new defined

    public static int LOG_MUA_VAT_PHAM = 113;
    public static int LOG_TAI_XIU = 54;
    public static int LOG_PHOM = 10004;
    public static int LOG_TLMN = 10005;
    public static int LOG_BACAY = 10011;
    public static int LOG_XI_TO = 10007;
    public static int LOG_SAM = 10037;
    public static int LOG_LIENG = 10009;
    public static int LOG_POKER = 10015;
    public static int LOG_VQMN = 11;
    public static int LOG_XOCDIA = 10010;
    public static int LOG_BINH = 10014;
    public static int LOG_SLOT_RONGVANG = 56;
    public static int LOG_SLOT_THUYCUNG = 57;
    public static int LOG_SLOT_TDK = 59;
    public static int LOG_SLOT_LONGTHAN = 60;
    public static int LOG_MINI_LARVAL = 1001;
    public static int LOG_MINI_POKER = 501;
    public static int LOG_GIFTCODE = 600;
    public static int LOG_LOGIN_DAY = 50;
    public static int LOG_XO_SO = 88;
    public static int LUCKEYSHOT = 9;
    public static int LOG_TYPE_TANG_TIEN = 111;
    public static int LOG_BY_PLAYING_EXIT_GAME_BIDA = 861;//dang choi thoat game
    public static int LOG_BY_ZONE_BIDA = 8;//game bida
    public static int LOG_BY_ZONE_FOOTBALL = 44;//game football
    public static int LOG_BY_ZONE_SOCCER_STARS = 45;//game soccer stars
    public static int LOG_BY_ZONE_HEAD_BALL = 46;//game soccer stars
    public static int LOG_BY_ZONE_SHOOTS = 48;//game ckcb
    public static int LOG_BY_ZONE_BIDA_PHOME = 86;//bida phom
    public static int LOG_BY_ZONE_DRAGGER = 47;//game phi dao
    public LogvascDB() {
    }

    public LogvascDB(Connection con) {
        this.conn = con;
    }

    public long insertLogVasc(long userID, double money, String Description, int LogTypeId, boolean isWin, int experience, double balanceAfter, long matchId, int matchNum, String isRealMoney,long cuoc, long owerid) throws Exception {
        Connection con = null;

        //con = DBPoolConnection.getConnection();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBVip.instance().getConnection();
            if (matchId <= 0) {
                matchId = 0;
            }
            if (matchNum <= 0) {
                matchNum = 0;
            }
            StringBuffer query = new StringBuffer();
            if (isRealMoney.equals(AIOConstants.PRIFIX_MONEY)) {
                query.append("INSERT INTO logvasc_money (userID,dateTime,money,description,logTypeId,balanceAfter,isWin,experience,matchId,matchNum,cuoc,ownerid) ");
                query.append("VALUES (? , ? , ? , ?, ?, ? , ? , ? , ?, ?, ?, ?)");
            } else {
                query.append("INSERT INTO logvasc (userID,dateTime,money,description,logTypeId,balanceAfter,isWin,experience,matchId,matchNum,cuoc,ownerid) ");
                query.append("VALUES (? , ? , ? , ?, ?, ? , ? , ? , ?, ?, ?, ?)");
            }

            stm = con.prepareStatement(query.toString(), 1);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String now = dateFormat.format(date);
            stm.setLong(1, userID);
            stm.setString(2, now);
            stm.setDouble(3, money);
            stm.setString(4, Description);
            stm.setInt(5, LogTypeId);
            stm.setDouble(6, balanceAfter);
            stm.setBoolean(7, isWin);
            stm.setInt(8, experience);
            stm.setLong(9, matchId);
            stm.setInt(10, matchNum);
            stm.setLong(11, cuoc);
            stm.setLong(12, owerid);

            stm.executeUpdate();
            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                long l = rs.getInt(1);
                return l;

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
        return 0;
    }

    public ArrayList<LogvascEntity> getLogVasc(long userID, int page, String isRealMoney) throws Exception {
        Connection con = null;

        con = DBPoolConnection.getConnection();

        PreparedStatement stm = null;
        ResultSet rs = null;
        ArrayList result = new ArrayList();
        try {
            //page = page;
            //}
            int items_per_page = 20;
            // build query
            int offset = (page - 1) * items_per_page;
            String limit = offset + "," + items_per_page;

            //StringBuilder query = new StringBuilder();
            //mLog.debug("logvas limit: " + limit);
            //query.append("select * from logvasc where userID =? ORDER BY id desc limit ").append(limit);
            String query = "select * from logvasc where userID =? ORDER BY id desc limit " + limit;
            if (isRealMoney.equals(AIOConstants.PRIFIX_MONEY)) {
                query = "select * from logvasc_money where userID =? ORDER BY id desc limit " + limit;
            }

            //mLog.debug("logvas: " + query);
            stm = con.prepareStatement(query);
            stm.setLong(1, userID);

            rs = stm.executeQuery();
            while (rs.next()) {
               
                LogvascEntity ne = new LogvascEntity();
                ne.id = rs.getLong("id");
                ne.userID = rs.getLong("userID");
                ne.dateTime = rs.getTimestamp("dateTime");
                ne.money = rs.getLong("money");
                ne.balanceAfter = rs.getLong("balanceAfter");
                ne.logTypeId = rs.getInt("logTypeId");
                ne.description = rs.getString("description");
                ne.isWin = rs.getInt("isWin");
                ne.cuoc = rs.getLong("cuoc");
                ne.ownerid = rs.getLong("ownerid");
                result.add(ne);
            }

        }catch(Exception e){
            //mLog.debug("error"+e.getMessage());
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
        return result;
    }

    public int checkLogVascLastLos(long userID, long logTypeId, String isRealMoney) throws Exception {
        Connection con = null;

        con = DBPoolConnection.getConnection();

        PreparedStatement stm = null;
        ResultSet rs = null;
        int result = 1;
        try {

            String query = "select * from logvasc where userID =? and logTypeId = ? ORDER BY id desc limit 1";
            if (isRealMoney.equals(AIOConstants.PRIFIX_MONEY)) {
                query = "select * from logvasc_money where userID =? and logTypeId = ? ORDER BY id desc limit 1";
            }
            //mLog.debug("logvas: " + query);
            stm = con.prepareStatement(query);
            stm.setLong(1, userID);
            stm.setLong(2, logTypeId);
            rs = stm.executeQuery();
            while (rs.next()) {

                result = rs.getInt("isWin");

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
        return result;
    }

    public static void main(String[] args) {
//        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//
//        try {
//            LogvascEntity loge = new LogvascEntity();
//            LogvascDB logev = new LogvascDB();
//            ArrayList<LogvascEntity> loges = logev.getLogVasc(312855, 1);
//            for (LogvascEntity ett : loges) {
//                String createdDate = dateFormat.format(ett.dateTime);
//                System.out.println("log:" + ett.description + " date:" + createdDate);
//            }
//            //loge = logev.getLogVasc(312855, 1);
//        } catch (Exception ex) {
//            java.util.logging.Logger.getLogger(LogvascDB.class.getName()).log(Level.SEVERE, null, ex);
//        }
        UserDB db = new UserDB();
        try {
            //List<AlertUserEntity> lstAlerts = db.getAlertUser(313858);

            for (int i = 0; i <= 1000; i++) {
//              UserEntity us =  db.login("zeptest1", "de88e3e4ab202d87754078cbb2df6063", "", "", "", "", 1, true);
//              System.out.println(i+":userid:"+us.mUsername);
                UserEntity us = db.getUserInfo(103, "realmoeny");
                System.out.println(i + ":us:" + us.mUsername);
            }

        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(LogvascDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
