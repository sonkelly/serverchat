/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.databaseDriven;

import allinone.data.Mail;
import static allinone.databaseDriven.ZoneDB.listZones;
import allinone.room.NZoneConfigEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Zeple
 */
public class MailDB {

    public static int getCountMail(long uid) {

        Connection con = null;

        PreparedStatement stm = null;
        ResultSet rs = null;
        int total = 0;
        try {
            con = DBGame.instance().getConnection();
            String query = "select num_not_read as total from users_mail where uid = ?";
            stm = con.prepareStatement(query);
            stm.setLong(1, uid);
            rs = stm.executeQuery();

            if (rs.first()) {
                total = rs.getInt("total");
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
        return total;
    }

    public ArrayList<Mail> receiveMail(long id) throws Exception {
        ArrayList<Mail> res = new ArrayList<Mail>();
        //String query = "{ call uspReceiveMail(?) }";
        String query = "SELECT * FROM  `mail` where destId = ? ORDER BY  `mail`.`mailId` DESC limit 10";

//        String query = "   SELECT TOP 20 [mailId],[sourceId],[sourceName],[destId],[title],[content],[isRead],[dateCreated],[eventId],[updateDate] " +
//        		       "   FROM [Mail] WHERE [destId] = " + id + 
//        		       "   ORDER BY [dateCreated] DESC ";        
//        System.out.println(" Query " + query);
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            //CallableStatement cs = conn.prepareCall(query);
            conn = DBGame.instance().getConnection();
            stm = conn.prepareStatement(query);
            stm.setLong(1, id);
            rs = stm.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    long mailId = rs.getLong("mailId");
                    long sID = rs.getLong("sourceId");
                    String sourceName = rs.getString("sourceName");
                    long dID = rs.getLong("destId");
                    String title = rs.getString("title");
                    String detail = rs.getString("content");
                    Date date = rs.getDate("dateCreated");
                    int isRead = rs.getInt("isRead");
                    int isAlert = rs.getInt("isAlert");

                    res.add(new Mail(mailId, sID, dID, detail, title, date, sourceName, isRead, isAlert));
                }
                rs.close();
            }
//            cs.clearParameters();
//            cs.close();
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

        return res;
    }

    //add by zep
    public ArrayList<Mail> getListMail(long id, String limit) throws Exception {
        ArrayList<Mail> res = new ArrayList<Mail>();
        //String query = "{ call uspReceiveMail(?) }";
        String query = "SELECT * FROM  `mail` where destId = ? ORDER BY  `mail`.`mailId` DESC limit " + limit;

        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = DBGame.instance().getConnection();
            //CallableStatement cs = conn.prepareCall(query);
            stm = conn.prepareStatement(query);
            stm.setLong(1, id);
            rs = stm.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    long mailId = rs.getLong("mailId");
                    long sID = rs.getLong("sourceId");
                    String sourceName = rs.getString("sourceName");
                    long dID = rs.getLong("destId");
                    String title = rs.getString("title");
                    String detail = rs.getString("content");
                    Date date = rs.getDate("dateCreated");
                    int isRead = rs.getInt("isRead");
                    int isAlert = rs.getInt("isAlert");

                    res.add(new Mail(mailId, sID, dID, detail, title, date, sourceName, isRead, isAlert));
                }
                rs.close();
            }
//            cs.clearParameters();
//            cs.close();
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

        return res;
    }

    //add by zep
    public Mail getMailDetail(long uid, long mailId) {

        long start = System.currentTimeMillis();
        Connection conn = null;
        Mail result = null;

        PreparedStatement stm = null;
        PreparedStatement stm_up = null;
        ResultSet rs = null;
        try {
            conn = DBGame.instance().getConnection();
            stm = conn.prepareStatement("SELECT * FROM  `mail` where destId = ? AND mailId = ? ORDER BY  `mail`.`mailId` DESC ");
            stm.setLong(1, uid);
            stm.setLong(2, mailId);
            rs = stm.executeQuery();
            if (rs.first()) {

                long sID = rs.getLong("sourceId");
                String sourceName = rs.getString("sourceName");
                long dID = rs.getLong("destId");
                String title = rs.getString("title");
                String detail = rs.getString("content");
                Date date = rs.getDate("dateCreated");
                int isRead = rs.getInt("isRead");
                int isAlert = rs.getInt("isAlert");
                result = new Mail(mailId, sID, dID, detail, title, date, sourceName, isRead, isAlert);
                //update isRead 
                if (isRead == 0) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date2 = new Date();
                    String now = dateFormat.format(date2);
                    StringBuffer query = new StringBuffer();

                    query.append("UPDATE mail SET isRead=1, dateRead=?  WHERE mailId=?");

                    stm_up = conn.prepareStatement(query.toString());
                    stm_up.setString(1, now);
                    stm_up.setLong(2, mailId);
                    stm_up.executeUpdate();
                    //update users_email
                    StringBuffer query_mail = new StringBuffer();
                    query_mail.append("UPDATE users_mail SET num_not_read=num_not_read-1 WHERE uid=?");
                    stm_up = conn.prepareStatement(query_mail.toString());
                    stm_up.setLong(1, uid);
                    stm_up.executeUpdate();
                    result.isRead = 1;
                }
                //update redis

            }
            long count = System.currentTimeMillis() - start;
            if (count > 100L) {

            }

            return result;
        } catch (SQLException e1) {
            System.out.println("error1:" + e1.getMessage());
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
                if (stm_up != null) {
                    stm_up.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return null;
    }
    //add by zep mail 
    public void insert(long destid,String systemsend, String title, String content) throws Exception {
        
        String query = "INSERT INTO mail (sourceId, sourceName, destId, title, content,dateCreated) VALUES (? , ? , ? , ? ,?,?)";


        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = DBGame.instance().getConnection();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String now = dateFormat.format(date);
            
            stm = conn.prepareStatement(query, 1);
            stm.setLong(1, 1);
            stm.setString(2, systemsend);
            stm.setLong(3, destid);
            stm.setString(4, title);
            stm.setString(5, content);
            stm.setString(6, now);
            
            stm.executeUpdate();
           

        } finally {
            if (conn != null) {
                conn.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

    }
    //add by zep users_mail 
    public void insertUserMail(long uid, int num_not_read) throws Exception {
        
        String query = "INSERT INTO users_mail (uid, num_not_read, date_log) VALUES (? , ? , ?)";


        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = DBGame.instance().getConnection();
            int now = (int) (System.currentTimeMillis() / 1000L);
            
            stm = conn.prepareStatement(query, 1);
            stm.setLong(1, uid);
            stm.setInt(2, num_not_read);
            stm.setInt(3, now);
            
            
            stm.executeUpdate();
           

        } finally {
            if (conn != null) {
                conn.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

    }
    public void updateCoundMailAdd(long uid)throws Exception {
        String query = "UPDATE users_mail SET num_not_read=num_not_read+1 WHERE uid=?";

        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        conn = DBGame.instance().getConnection();
        try {
            
            stm = conn.prepareStatement(query.toString());
            stm.setLong(1, uid);
            stm.executeUpdate();
           

        }catch (SQLException e1) {
            System.out.println("error1:" + e1.getMessage());
        }  finally {
            if (conn != null) {
                conn.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        
    }
}
