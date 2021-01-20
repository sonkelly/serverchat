/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.databaseDriven;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

import allinone.data.AIOConstants;
import allinone.data.UserEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import vn.game.session.ISession;
/**
 *
 * @author Zeple
 */
public class GiftMoneyAuto {

    public static int getCountGift(long uid,int campaignid) {

        Connection con = null;

        PreparedStatement stm = null;
        ResultSet rs = null;
        int total = 0;
        try {
            con = DBGame.instance().getConnection();
            String query = "select count(*) as total from gift_money_auto where uid = ? and campaignid = ?";
            stm = con.prepareStatement(query);
            stm.setLong(1, uid);
            stm.setLong(2, campaignid);
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
    public static long addLog(long uid,String viewname, long money, int campaignid) throws Exception {

        //String query = "{?= call uspRegUser(?,?,?,?,?,?,?,?,?,?,?,?) }";
        String query = "INSERT INTO gift_money_auto (uid, viewname, money, campaignid, createdtime) VALUES (? , ? , ? , ?, ? )";

        //Connection con = DBPoolConnectionGame.getConnection();
        Connection con = null;

        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBGame.instance().getConnection();
            stm = con.prepareStatement(query, 1);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String now = dateFormat.format(date);

            stm.setLong(1, uid);
            stm.setString(2, viewname);
            stm.setLong(3, money);
            stm.setInt(4, campaignid);
            stm.setString(5, now);
            stm.executeUpdate();

            rs = stm.getGeneratedKeys();
            long lid = 0;
            if (rs.next()) {
                lid = rs.getLong(1);
            }
            return lid;

        } finally {
            con.close();
        }
    }
    public static long processGiftAuto(int campaignid,UserEntity entity, String realmoney){
        long uid = entity.mUid;
        int countGift = GiftMoneyAuto.getCountGift( uid, campaignid);
        long curMoney = 0;
        if(countGift <= 0 ){
            try {
               long lid = GiftMoneyAuto.addLog( uid, entity.viewName,AIOConstants.GIFT_SUPPORT_MONEY,1);
               if(lid > 0){
                    curMoney  = new UserDB().updateUserMoney(AIOConstants.GIFT_SUPPORT_MONEY, true, uid, ""+AIOConstants.GIFT_SUPPORT_MONEY_MIN, 0, AIOConstants.LOG_TYPE_TANG_TIEN, 0, 0,realmoney,0,0);
                  
               }
            } catch (Exception ex) {
                Logger.getLogger(GiftMoneyAuto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return curMoney;
        
    }
}
