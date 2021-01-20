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
public class PhoneDb {

    public static String getPhone(long uid) {

        Connection con = null;

        PreparedStatement stm = null;
        ResultSet rs = null;
        String phone = "";
        try {
            con = DBGame.instance().getConnection();
            String query = "select * from phone where uid = ?";
            stm = con.prepareStatement(query);
            stm.setLong(1, uid);
            rs = stm.executeQuery();

            if (rs.first()) {
                phone = rs.getString("phone");
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
        return phone;
    }

}
