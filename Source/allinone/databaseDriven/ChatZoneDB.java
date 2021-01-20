/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.databaseDriven;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import tools.CacheEventInfo;
import vn.game.common.LoggerContext;
import allinone.data.AIOConstants;
import allinone.data.EventBonusEntity;
import allinone.data.EventEntity;
import allinone.data.EventPlayerEntity;
import allinone.data.GoldenHourEntity;
import allinone.data.Mail;
import allinone.data.TXChat;
import allinone.data.UserEntity;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import vn.game.entity.ChatZoneUtity;


/**
 *
 * @author mac
 */
public class ChatZoneDB {
    private static final Logger mLog = LoggerContext.getLoggerFactory()
            .getLogger(ChatZoneDB.class);
    
    public ArrayList<ChatZoneUtity> getChats(int pageIndex,int zoneid) throws Exception {
        if (pageIndex <= 0) {
            pageIndex = 1;
        }
        int items_per_page = 20;
        // build query
        int offset = (pageIndex - 1) * items_per_page;
        String limit = offset + "," + items_per_page;
        
        ArrayList<ChatZoneUtity> res = new ArrayList<ChatZoneUtity>();
        String query = "SELECT * FROM  `chatzone` where zoneid = ? ORDER BY  `id` DESC limit " + limit;
       
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = DBVip.instance().getConnection();
           
            stm = conn.prepareStatement(query);
            stm.setInt(1, zoneid);
            
            rs = stm.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                   
                    String viewname = rs.getString("viewname");
                    String message = rs.getString("message");
                    res.add(new ChatZoneUtity(viewname,message));
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
    public void addChat(int zoneid, long uid, String _viewname, String _msg) throws Exception {
        boolean res = false;
        String query = "INSERT INTO chatzone (uid,viewname,message,createdtime, zoneid) VALUES (? , ?, ?, ?, ?)";

        Connection con = null;

        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBVip.instance().getConnection();
            stm = con.prepareStatement(query, 1);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String now = dateFormat.format(date);
            
            stm.setLong(1, uid);
            stm.setString(2, _viewname);
            stm.setString(3, _msg);
          
            stm.setString(4, now);
            stm.setInt(5, zoneid);
            stm.executeUpdate();


        }catch(Exception e){
            mLog.debug("error:"+e.getMessage());
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
}
