/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.tournement;

import static allinone.data.SimpleTable.mLog;
import allinone.data.UserEntity;
import allinone.databaseDriven.DBAdmin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 *
 * @author mac
 */
public class AutouTourBidaUserDB {

    public long regsiterTour(UserEntity user, TournementEntity tour) {
        Connection con = null;
        long registerId = 0;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            //get userinfo 

            con = DBAdmin.instance().getConnection();

            StringBuffer query = new StringBuffer();
            query.append("INSERT INTO autotourBida_user (uid,viewname,name, money,tourID,createdtime) ");
            query.append("VALUES (? , ? , ?, ? , ? , ? )");
            stm = con.prepareStatement(query.toString(), 1);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String now = dateFormat.format(date);
            stm.setLong(1, user.mUid);
            stm.setString(2, user.viewName);
            stm.setString(3, tour.name);
             stm.setLong(4, tour.money_coc);
            stm.setLong(5, tour.id);
            stm.setString(6, now);

            stm.executeUpdate();
            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                registerId = rs.getInt(1);

            }

        } catch (Exception e) {
            mLog.debug("error:" + e.getMessage());
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
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(AutoTourBidaDB.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return registerId;
    }
}
