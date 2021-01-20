package allinone.databaseDriven;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import vn.com.landsoft.common.ILoggerFactory;
import vn.com.landsoft.common.LoggerContext;

public class XocDiaDB {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(XocDiaDB.class);

    public static boolean insertResult(int result, int roomid, int numwhite, int numblack, int createdtime, double totalMoney, double phe)
            throws SQLException {
        String query = "INSERT INTO xocdia_game_result(roomid, result,numwhite,numblack,totalMoney,phe,createdtime) VALUES (?,?,?,?,?,?,?)";
        Connection con = null;
        PreparedStatement psmt = null;
        int roundAffected = 0;
        try {

            con = DBVip.instance().getConnection();
            psmt = con.prepareStatement(query);
            psmt.setInt(1, roomid);
            psmt.setInt(2, result);
            psmt.setInt(3, numwhite);
            psmt.setInt(4, numblack);
            psmt.setDouble(5, totalMoney);
            psmt.setDouble(6, phe);
            psmt.setInt(7, createdtime);
            roundAffected = psmt.executeUpdate();
            if (roundAffected > 0) {
                return true;
            }
        } catch (SQLException e) {
            mLog.info("Errors when insertResult : " + roomid + "|" + result + "|"  + e.getMessage());
        } finally {
            try {
                if (psmt != null) {
                    psmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception localException2) {
            }
        }
        try {
            if (psmt != null) {
                psmt.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception localException3) {
        }
        return false;
    }

    public static ArrayList<Integer> getTopResult(int limit,int roomid) throws SQLException {
        ArrayList<Integer> result = new ArrayList();
        String query = "SELECT result FROM xocdia_game_result WHERE roomid = ? ORDER BY id DESC limit " + limit;
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try {
            con = DBVip.instance().getConnection();
            psmt = con.prepareStatement(query);
            psmt.setInt(1, roomid);
            rs = psmt.executeQuery();
            while (rs.next()) {
                result.add(rs.getInt("result"));
            }
        } catch (SQLException var7_7) {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (psmt != null) {
                    psmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception localException) {
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (psmt != null) {
                    psmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception localException1) {
            }
        }
        return result;
    }

}
