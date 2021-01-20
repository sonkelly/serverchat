/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.tournement;

import static allinone.data.SimpleTable.mLog;
import allinone.databaseDriven.DBAdmin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author mac
 */
public class AutoTourBidaTemplateDB {
    public AutoTourBidaTemplateDB() {
       
    }
    public TourTemplateEntity getTemplate(int gameID) throws SQLException {
        TourTemplateEntity template = null;
    
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
        
            conn = DBAdmin.instance().getConnection();
           
            stm = conn.prepareStatement("SELECT * FROM  `autotourBidaTemplate` WHERE gameId = ? and `status` = ? limit 1");
            stm.setInt(1, gameID);
            stm.setInt(2, 1);
            rs = stm.executeQuery();
            while (rs.next()) {

                int id = rs.getInt("id");

                String name = rs.getString("name");
                int gameId = rs.getInt("gameId");
                int creatorId = rs.getInt("creatorId");
                int money_coc = rs.getInt("money_coc");
                int money_cuoc = rs.getInt("money_cuoc");
                int statusID = rs.getInt("status");
                int moneyType = rs.getInt("moneyType");
                
                Date date = rs.getDate("createdtime");
              
                template = new TourTemplateEntity(id, date, date, name, money_coc, money_cuoc, gameId, creatorId, statusID, moneyType);
              
               
            }

            // return result;
        } catch (SQLException e1) {
            mLog.debug("error temp:"+e1.getMessage());
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
               
            }
        }
        return template;

    }
}
