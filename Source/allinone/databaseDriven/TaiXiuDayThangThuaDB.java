/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import allinone.data.Mail;
import allinone.data.TXChat;
import allinone.data.TaiXiuDayThangThuaEntity;
import allinone.data.UserEntity;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 *
 * @author mac
 */
public class TaiXiuDayThangThuaDB {

    private static final Logger mLog = LoggerContext.getLoggerFactory()
            .getLogger(TaiXiuDayThangThuaDB.class);

    public static TaiXiuDayThangThuaEntity getLog(long uid) throws Exception {
        TaiXiuDayThangThuaEntity dudayObj = null;
        String query = "SELECT * FROM `taixiu_duday_log` where uid =? and createdtime >= ? and createdtime <= ? and isgift = 0 order by id desc limit 1";

        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBAdmin.instance().getConnection();
            stm = con.prepareStatement(query);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String now = dateFormat.format(date) + " 00:00:00";
            String nowend = dateFormat.format(date) + " 23:59:59";
            stm.setLong(1, uid);
            stm.setString(2, now);
            stm.setString(3, nowend);
            rs = stm.executeQuery();

            if (rs.first()) {
                int id = rs.getInt("id");
                String viewname = rs.getString("viewname");
                int daythang = rs.getInt("daythang");
                int daythua = rs.getInt("daythua");
                Date createdtimeObj = rs.getTimestamp("createdtime");
                String createdtime = dateFormat.format(createdtimeObj);

                int moneywin = rs.getInt("moneywin");
                int moneylos = rs.getInt("moneylos");
                int utype = rs.getInt("utype");
                int totalbet = rs.getInt("totalbet");
                int countgift = rs.getInt("countgift");
                int lasttaixiu = rs.getInt("lasttaixiu");
                dudayObj = new TaiXiuDayThangThuaEntity(id, uid, viewname, daythang, daythua, createdtime,
                        moneywin, moneylos, utype, totalbet, countgift, lasttaixiu);

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
        return dudayObj;
    }

    public static TaiXiuDayThangThuaEntity insertLog(long uid, String _viewname, long daythang, long daythua,
            int utype, long totalBet, int moneywin, int moneylos, int lasttaixiu) throws Exception {
        TaiXiuDayThangThuaEntity dudayObj = null;
        String query = "INSERT INTO taixiu_duday_log (uid,viewname,daythang,daythua,createdtime, utype,totalbet, lasttaixiu) VALUES (? , ?, ?,?,?, ?,?,?)";

        Connection con = null;

        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBAdmin.instance().getConnection();
            stm = con.prepareStatement(query, 1);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String now = dateFormat.format(date);

            stm.setLong(1, uid);
            stm.setString(2, _viewname);
            stm.setLong(3, daythang);
            stm.setLong(4, daythua);
            stm.setString(5, now);
            stm.setInt(6, utype);
            stm.setLong(7, totalBet);
            stm.setInt(8, lasttaixiu);
            stm.executeUpdate();
            dudayObj = new TaiXiuDayThangThuaEntity(0, uid, _viewname, (int) daythang, (int) daythua, now,
                    (int) moneywin, (int) moneylos, utype, (int) totalBet, 0, lasttaixiu);

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
        return dudayObj;
    }

    public static void updateLog(TaiXiuDayThangThuaEntity dudayObj) throws Exception {
        //TaiXiuDayThangThuaEntity dudayObj = null;
        String query = "UPDATE taixiu_duday_log set daythang = ?, daythua = ?, totalbet=?, moneywin = ?, moneylos = ?, updatetime =?, lasttaixiu =? where id= ? and uid = ? and isgift = 0";

        Connection con = null;

        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBAdmin.instance().getConnection();
            stm = con.prepareStatement(query, 1);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String now = dateFormat.format(date);

            stm.setLong(1, dudayObj.getDaythang());
            stm.setLong(2, dudayObj.getDaythua());
            stm.setLong(3, dudayObj.getTotalbet());
            stm.setLong(4, dudayObj.getMoneywin());
            stm.setLong(5, dudayObj.getMoneylost());
            stm.setString(6, now);
            stm.setLong(7, dudayObj.lasttaixiu);
            stm.setLong(8, dudayObj.id);
            stm.setLong(9, dudayObj.uid);
            stm.executeUpdate();

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
        //return dudayObj;
    }
}
