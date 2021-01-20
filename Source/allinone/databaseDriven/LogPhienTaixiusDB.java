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
import java.util.logging.Level;
import org.slf4j.Logger;
import vn.game.common.LoggerContext;

/**
 *
 * @author mac
 */
public class LogPhienTaixiusDB {

    private static final Logger mLog = LoggerContext.getLoggerFactory()
            .getLogger(LogPhienTaixiusDB.class);

    public static long insertLogPhien(long uid, String viewname, long totalMoneyBet, long totalRefund, long totalWin, long idphien, int isBet) throws Exception {
        Connection con = null;

        //con = DBPoolConnection.getConnection();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBGame.instance().getConnection();

            StringBuffer query = new StringBuffer();
            query.append("INSERT INTO log_phien_taixius (uid,viewname,totalMoneyBet, totalRefund,totalWin, isBet,idphien,createdtime) ");
            query.append("VALUES (? , ? , ? , ?, ?, ? , ? , ? )");

            stm = con.prepareStatement(query.toString(), 1);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String now = dateFormat.format(date);
            stm.setLong(1, uid);

            stm.setString(2, viewname);
            stm.setLong(3, totalMoneyBet);
            stm.setLong(4, totalRefund);
            stm.setLong(5, totalWin);
            stm.setInt(6, isBet);
            stm.setLong(7, idphien);
            stm.setString(8, now);

            stm.executeUpdate();
            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                long l = rs.getInt(1);
                return l;

            }

        } catch (SQLException e) {
            mLog.debug("log tx err" + e.getMessage());
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

    public static void main(String[] args) {
        try {
            LogPhienTaixiusDB.insertLogPhien(1, "1", 0, 0, 0, 0, 0);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(LogPhienTaixiusDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
