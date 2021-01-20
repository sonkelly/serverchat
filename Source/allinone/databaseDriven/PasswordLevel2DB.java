/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.databaseDriven;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.db.DBException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;

/**
 *
 * @author Zeple
 */
@SuppressWarnings("unused")
public class PasswordLevel2DB {

    private static final Logger mLog = LoggerContext.getLoggerFactory()
            .getLogger(UserDB.class);
    private Connection conn;

    public PasswordLevel2DB() {
    }

    public PasswordLevel2DB(Connection con) {
        this.conn = con;
    }

    public boolean createPassLevel2(long uid, String password) throws Exception {

        boolean res = false;
        String query = "INSERT INTO users_pass_level2 (uid,pass) VALUES (? , ? )";

        Connection con = null;

        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBGame.instance().getConnection();
            stm = con.prepareStatement(query, 1);

            
            stm.setLong(1, uid);
            stm.setString(2, password);
            

            stm.executeUpdate();

            rs = stm.getGeneratedKeys();
           
            if (rs.next()) {
                res = true;
            }
            return res;

        } finally {
            if(con != null){
                con.close();
            }
            if(stm != null){
                stm.close();
            }
            if(rs != null){
                rs.close();
            }
        }
    }
    public int changePassword(long userId, String oldPassword, String newPassword) throws DBException, SQLException {
        String query = "Update users_pass_level2 SET pass = ? WHERE uid = ? AND pass = ?";
        Connection con = null;
        CallableStatement cs = null;
        int ret = 0;

        try {
            con = DBGame.instance().getConnection();
             cs = con.prepareCall(query);
            cs.setString(1, newPassword);
            cs.setLong(2, userId);
            cs.setString(3, oldPassword);
            ret = cs.executeUpdate();

            mLog.debug(" return value " + ret);

        } finally {
            if(con != null){
                con.close();
            }
            if(cs != null){
                cs.close();
            }
            
        }

        return ret;
    }
    public String getPasswordLevel2(long userID) throws Exception {
        String pass ="";
        Connection con = null;


        StringBuffer query = new StringBuffer();
        query.append("SELECT * FROM users_pass_level2 WHERE uid=?");

        ResultSet rs = null;
        CallableStatement cs = null;
        PreparedStatement stm = null;
        try {
            con = DBGame.instance().getConnection();
            stm = con.prepareStatement(query.toString());

            stm.setLong(1, userID);
           

            rs = stm.executeQuery();
            mLog.debug(rs.toString());
            if (rs.first()) {
              
                pass = rs.getString("pass");

            }else{
                pass ="chuathietlap";
            }

        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (stm != null) {
                    stm.close();
                }

                if (rs != null) {
                    rs.close();
                }

            } catch (Exception e) {
                mLog.debug("Login Exception:", e);
            }

        }

        return pass;
    }

    public static void main(String[] args) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        try {
           
            PasswordLevel2DB logev = new PasswordLevel2DB();
            String pass = logev.getPasswordLevel2(103);
            System.out.println("pass:" + pass);
            //loge = logev.getLogVasc(312855, 1);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(LogvascDB.class.getName()).log(Level.SEVERE, null, ex);
        }
//        UserDB db = new UserDB();
//        try {
//            List<AlertUserEntity> lstAlerts = db.getAlertUser(313858);
//        } catch (SQLException ex) {
//            java.util.logging.Logger.getLogger(LogvascDB.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
