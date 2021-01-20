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

public class MiniGameXocDiaDB {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(MiniGameXocDiaDB.class);

    public static long insertResult(String result, String result_values,
            long numWhite, long numBlack, long bontrang, long bonden, long batrangmotden, long badenmottrang, long chan, long le)
            throws SQLException {

        String query = "INSERT INTO mini_game_xocdia (result,result_values,numWhite,numBlack,bontrang,bonden,batrangmotden, badenmottrang,chan,le,creadtime) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        Connection con = null;
        PreparedStatement psmt = null;

        ResultSet rs = null;

        long generatedID = -1;
        try {
            //con = DBPoolConnection.getConnection();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String now = dateFormat.format(date);

            con = DBVip.instance().getConnection();
            psmt = con.prepareStatement(query);
            psmt.setString(1, result);
            psmt.setString(2, result_values);

            psmt.setLong(3, numWhite);
            psmt.setLong(4, numBlack);
            psmt.setLong(5, bontrang);
            psmt.setLong(6, bonden);
            psmt.setLong(7, batrangmotden);
            psmt.setLong(8, badenmottrang);
            psmt.setLong(9, chan);
            psmt.setLong(10, le);
            psmt.setString(11, now);

            psmt.executeUpdate();
            rs = psmt.getGeneratedKeys();
            if (rs.next()) {
                generatedID = rs.getInt(1);

            }
            if (generatedID > 0) {

                return generatedID;
            }

        } catch (SQLException e) {
            //mLog.info("Errors when insertResult : " + gameID + "|" + result + "|" + gameID + "| e : " + e.getMessage());
        } finally {
            try {
                if (psmt != null) {
                    psmt.close();
                }
                if (con != null) {
                    con.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception localException2) {
                //mLog.info("Errors when insertResult : " + gameID + "|" + result + "|" + gameID + "| e : " + localException2.getMessage());
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
            //mLog.info("Errors when insertResult : " + gameID + "|" + result + "|" + gameID + "| e : " + localException3.getMessage());
        }
        return generatedID;
    }

    /* Error */
    public static long matchNumber()  {
        long result = 0L;
        //String query = "SELECT count(*) FROM mini_game_result WHERE gameid=?";
        String query = "SELECT id FROM mini_game_xocdia order by id desc limit 1";

        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try {
            con = DBVip.instance().getConnection();
            psmt = con.prepareStatement(query);
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

    public static ArrayList<String> getTopResult(int limit) throws SQLException {
        ArrayList<String> result = new ArrayList();
        String query = "SELECT id,result,result_values,total,numWhite,numBlack FROM mini_game_xocdia ORDER BY id DESC limit " + limit;
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try {
            con = DBVip.instance().getConnection();
            psmt = con.prepareStatement(query);
            rs = psmt.executeQuery();
            while (rs.next()) {

                //txe = new Txdata(rs.getLong("id"),rs.getInt("result"),rs.getString("result_values"),rs.getInt("total"));
                String resStr = rs.getInt("result") + ":" + rs.getInt("numWhite") + ":" + rs.getInt("numBlack") + "#";
                //mLog.debug("resStr:"+resStr);
                result.add(resStr);

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
