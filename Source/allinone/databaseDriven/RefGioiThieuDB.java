package allinone.databaseDriven;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import vn.com.landsoft.common.LoggerContext;

public class RefGioiThieuDB
{
  public RefGioiThieuDB() {}
  
  private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(RefGioiThieuDB.class);
  
  public static boolean checkUserRef(long uid) throws SQLException {
    String query = "SELECT count(userId) FROM workinguser WHERE userId=?";
    Connection con = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    try {
      con = DBPoolConnection.getConnection();
      psmt = con.prepareStatement(query);
      psmt.setLong(1, uid);
      rs = psmt.executeQuery();
      if ((rs.next()) && (rs.getInt(1) > 0)) return true;
    } catch (SQLException e) {
      mLog.info("Errors when checkUserRef : " + uid + e.getMessage());
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
    
    return false;
  }
  
  public static boolean checkCanReference(long uid, String device, String ip) throws SQLException {
    String query = "SELECT count(*) FROM refgioithieu WHERE uid=?";
    if (device.length() < 10) device = "";
    if (device != "") {
      query = query + " OR device=? ";
    }
    query = query + " OR ip=?";
    Connection con = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    try {
      con = DBPoolConnection.getConnection();
      psmt = con.prepareStatement(query);
      psmt.setLong(1, uid);
      if (device != "") {
        psmt.setString(2, device);
        psmt.setString(3, ip);
      } else {
        psmt.setString(2, ip);
      }
      rs = psmt.executeQuery();
      if (rs.next()) {
        if (rs.getInt(1) > 0)
          return false;
        return true;
      }
      return true;
    }
    catch (SQLException e) {
      mLog.info("Errors when checkReference : " + uid + "|" + device + "|" + ip + "| e : " + e.getMessage());
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
      catch (Exception localException4) {}
    }
    return false;
  }
  
  public static boolean updateReference(long uid, long uidRef, String device, String ip) throws SQLException {
    String query = "INSERT INTO  refgioithieu(uid,uid_ref";
    if (device.length() < 10) device = "";
    if (device != "") {
      query = query + ",device";
    }
    query = query + ",ip) VALUES(?,?";
    if (device != "") {
      query = query + ",?";
    }
    query = query + ",?)";
    Connection con = null;
    PreparedStatement psmt = null;
    int roundAffected = 0;
    try {
      con = DBPoolConnection.getConnection();
      psmt = con.prepareStatement(query);
      psmt.setLong(1, uid);
      psmt.setLong(2, uidRef);
      if (device != "") {
        psmt.setString(3, device);
        psmt.setString(4, ip);
      } else {
        psmt.setString(3, ip);
      }
      roundAffected = psmt.executeUpdate();
      if (roundAffected > 0) return true;
    } catch (SQLException e) {
      mLog.info("Errors when updateReference : " + uid + "|" + device + "|" + ip + "| e : " + e.getMessage());
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
}
