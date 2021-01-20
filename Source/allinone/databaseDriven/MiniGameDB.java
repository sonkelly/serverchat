package allinone.databaseDriven;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import vn.com.landsoft.common.ILoggerFactory;
import vn.com.landsoft.common.LoggerContext;

public class MiniGameDB {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(MiniGameDB.class);

    public static long insertResult(String result, int gameID, long totalMoney, long phe,String result_values,int result_number,
            long totalHoanTai,long moneyPlayTai,long totalHoanXiu,long moneyPlayXiu,String jsonTaiUsers, String jsonXiuUsers)
            throws SQLException {
       
        String query = "INSERT INTO mini_game_result(result,gameid,totalMoney,phe,result_values,total,logtais,logxius,taihoan,taidat,xiuhoan, xiudat,creadtime) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection con = null;
        PreparedStatement psmt = null;
       
        ResultSet rs = null;
//        totalHoanTai = totalHoanTai < 0 ? 0 : totalHoanTai;
//        totalHoanXiu = totalHoanXiu < 0 ? 0 : totalHoanXiu;
        long generatedID = -1;
        try {
            //con = DBPoolConnection.getConnection();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String now = dateFormat.format(date);
            
            con = DBVip.instance().getConnection();
            psmt = con.prepareStatement(query);
            psmt.setString(1, result);
            psmt.setInt(2, gameID);
            psmt.setLong(3, totalMoney);
            psmt.setLong(4, phe);
            psmt.setString(5, result_values);
            psmt.setInt(6, result_number);
            psmt.setString(7, jsonTaiUsers);
            psmt.setString(8, jsonXiuUsers);
            psmt.setLong(9, totalHoanTai);
            psmt.setLong(10, moneyPlayTai);
            psmt.setLong(11, totalHoanXiu);
            psmt.setLong(12, moneyPlayXiu);
            psmt.setString(13, now);
            
            psmt.executeUpdate();
            rs = psmt.getGeneratedKeys();
            if (rs.next()) {
                generatedID = rs.getInt(1);

            }
            if (generatedID > 0) {
              
                return generatedID;
            }
            
        } catch (SQLException e) {
            mLog.info("Errors when insertResult : " + gameID + "|" + result + "|" + gameID + "| e : " + e.getMessage());
        } finally {
            try {
                if (psmt != null) {
                    psmt.close();
                }
                if (con != null) {
                    con.close();
                }
                if(rs != null){
                    rs.close();
                }
            } catch (Exception localException2) {
                mLog.info("Errors when insertResult : " + gameID + "|" + result + "|" + gameID + "| e : " + localException2.getMessage());
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
            mLog.info("Errors when insertResult : " + gameID + "|" + result + "|" + gameID + "| e : " + localException3.getMessage());
        }
        return generatedID;
    }

    /* Error */
    public static long matchNumber(int gameID) throws SQLException {
        long result = 0L;
        //String query = "SELECT count(*) FROM mini_game_result WHERE gameid=?";
        String query = "SELECT id FROM mini_game_result WHERE gameid=? order by id desc limit 1";
        
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try {
            con = DBVip.instance().getConnection();
            psmt = con.prepareStatement(query);
            psmt.setInt(1, gameID);
            rs = psmt.executeQuery();
            while (rs.next()) {
                result = rs.getInt(1);// 1 cho phien tiep theo
            }
        } catch (SQLException var7_6) {
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

    /* Error */
//    public static String[] getTopResult(int gameID, int limit)
//            throws SQLException {
//        //return null;
//        List<String> stockList = new ArrayList<String>();
//        stockList.add("1");
//        stockList.add("1");
//        stockList.add("0");
//        stockList.add("0");
//
//        String[] stockArr = new String[stockList.size()];
//        stockArr = stockList.toArray(stockArr);
//        return stockArr;
//    }
    ///dung cho xoc dia
//    public static boolean insertResultXocDia(String result, int gameID, long totalMoney, long phe)
//            throws SQLException {
//        String query = "INSERT INTO mini_game_result(result,gameid,totalMoney,phe) VALUES (?,?,?,?)";
//        Connection con = null;
//        PreparedStatement psmt = null;
//        int roundAffected = 0;
//        try {
//            //con = DBPoolConnection.getConnection();
//            con = DBVip.instance().getConnection();
//            psmt = con.prepareStatement(query);
//            psmt.setString(1, result);
//            psmt.setInt(2, gameID);
//            psmt.setLong(3, totalMoney);
//            psmt.setLong(4, phe);
//            roundAffected = psmt.executeUpdate();
//            if (roundAffected > 0) {
//                return true;
//            }
//        } catch (SQLException e) {
//            mLog.info("Errors when insertResult : " + gameID + "|" + result + "|" + gameID + "| e : " + e.getMessage());
//        } finally {
//            try {
//                if (psmt != null) {
//                    psmt.close();
//                }
//                if (con != null) {
//                    con.close();
//                }
//            } catch (Exception localException2) {
//            }
//        }
//        try {
//            if (psmt != null) {
//                psmt.close();
//            }
//            if (con != null) {
//                con.close();
//            }
//        } catch (Exception localException3) {
//        }
//        return false;
//    }
    
}
