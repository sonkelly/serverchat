package allinone.databaseDriven;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import vn.com.landsoft.common.LoggerContext;

public class ReferenceCodesDB
{
  public ReferenceCodesDB() {}
  
  private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(ReferenceCodesDB.class);
  
  public static boolean updateRefCode(String refCode) throws SQLException {
    String query = "UPDATE reference_codes SET used=used+1 WHERE reference_code=?";
    Connection con = null;
    PreparedStatement psmt = null;
    int roundAffected = 0;
    try {
      con = DBPoolConnection.getConnection();
      psmt = con.prepareStatement(query);
      psmt.setString(1, refCode);
      roundAffected = psmt.executeUpdate();
      if (roundAffected > 0) return true;
    } catch (SQLException e) {
      mLog.info("Errors when updateRefCode :  " + e.getMessage());
    } finally {
      try {
        if (psmt != null) {
          psmt.close();
        }
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException2) {}
    }
    try
    {
      if (psmt != null) {
        psmt.close();
      }
      if (con != null) {
        con.close();
      }
    }
    catch (Exception localException3) {}
    
    return false;
  }
  
  public static boolean insertRefCode(String refCode, long uid) throws SQLException {
    String query = "INSERT INTO reference_codes(reference_code,uid,used) VALUES (?,?,?)";
    Connection con = null;
    PreparedStatement psmt = null;
    int roundAffected = 0;
    try {
      con = DBPoolConnection.getConnection();
      psmt = con.prepareStatement(query);
      psmt.setString(1, refCode);
      psmt.setLong(2, uid);
      psmt.setLong(3, 0L);
      roundAffected = psmt.executeUpdate();
      if (roundAffected > 0) return true;
    } catch (SQLException e) {
      mLog.info("Errors when insertRefCode : " + uid + "|" + refCode + "|" + uid + "| e : " + e.getMessage());
    } finally {
      try {
        if (psmt != null) {
          psmt.close();
        }
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException2) {}
    }
    try
    {
      if (psmt != null) {
        psmt.close();
      }
      if (con != null) {
        con.close();
      }
    }
    catch (Exception localException3) {}
    
    return false;
  }
  
  public static String getCodeFromUid(long uid) throws SQLException {
    String result = "";
    String query = "SELECT reference_code FROM reference_codes WHERE uid=?";
    Connection con = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    try {
      con = DBPoolConnection.getConnection();
      psmt = con.prepareStatement(query);
      psmt.setLong(1, uid);
      rs = psmt.executeQuery();
      if (rs.next()) return rs.getString("reference_code");
    } catch (SQLException e) {
      mLog.info("Errors when getCodeFromUid : " + uid + e.getMessage());
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
      }
      catch (Exception localException2) {}
    }
    try
    {
      if (rs != null) {
        rs.close();
      }
      if (psmt != null) {
        psmt.close();
      }
      if (con != null) {
        con.close();
      }
    }
    catch (Exception localException3) {}
    
    return result;
  }
  
  public static long checkRefCode(String refCode) throws SQLException {
    String query = "SELECT uid FROM reference_codes WHERE reference_code=?";
    Connection con = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    try {
      con = DBPoolConnection.getConnection();
      psmt = con.prepareStatement(query);
      psmt.setString(1, refCode);
      rs = psmt.executeQuery();
      if (rs.next()) return rs.getInt("uid");
    } catch (SQLException e) {
      mLog.info("Errors when checkRefCode : " + refCode + e.getMessage());
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
      }
      catch (Exception localException2) {}
    }
    try
    {
      if (rs != null) {
        rs.close();
      }
      if (psmt != null) {
        psmt.close();
      }
      if (con != null) {
        con.close();
      }
    }
    catch (Exception localException3) {}
    
    return -1L;
  }
}
