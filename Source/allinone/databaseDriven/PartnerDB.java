/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.databaseDriven;

import allinone.data.Mail;
import allinone.data.SuperUserEntity;
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
public class PartnerDB {

    //add by zep
    public ArrayList<SuperUserEntity> getListPartner() throws Exception {
        ArrayList<SuperUserEntity> res = new ArrayList<SuperUserEntity>();
        String query = "SELECT * FROM  `partner` where status = ? ORDER BY  id desc limit 50";

        Connection conn = DBPoolConnection.getConnection();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            
            stm = conn.prepareStatement(query);
            stm.setLong(1, 1);
            rs = stm.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    long id = rs.getLong("id");
                    long uid = rs.getLong("uid");
                    String username = rs.getString("username");
                    
                    res.add(new SuperUserEntity(id, uid, username));
                }
                rs.close();
            }

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

}
