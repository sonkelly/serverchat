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
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;

import tools.CacheUserInfo;
import vn.game.common.LoggerContext;
import vn.game.common.MD5;
import vn.game.db.DBException;
import allinone.data.AlertUserEntity;
import allinone.data.GameDataEntity;
import allinone.data.LogvascEntity;
import allinone.data.SimplePlayer;
import allinone.data.SimpleTable;
import static allinone.data.SimpleTable.LOG_TYPE_GAME_START;
import allinone.data.UserEntity;
import allinone.data.UserInfoEntity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import static tools.CacheUserInfo.intToByteArray;

/**
 *
 * @author Zeple
 */
@SuppressWarnings("unused")
public class LogGameDataDB {

    private static final Logger mLog = LoggerContext.getLoggerFactory()
            .getLogger(LogGameDataDB.class);
    private Connection conn;

    public LogGameDataDB() {
    }

    public LogGameDataDB(Connection con) {
        this.conn = con;
    }

    public long insertLogGameData(long userID, int zoneId, boolean iswin, int exp, int typeMoney) throws Exception {
        //Connection con = null;

        //con = DBPoolConnection.getConnection();
        int win = 0;
        int lost = 0;
        if(iswin){
            win = 1;
        }else{
            lost = 1;
        }
        PreparedStatement stm = null;
        ResultSet rs = null;
        //int zoneId = zoneIdFull-SimpleTable.LOG_TYPE_GAME_START;
        long logRs = 0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String now = dateFormat.format(date);
        try {
            this.conn = DBVip.instance().getConnection();
            boolean checkExistLog = this.checkExistLog( userID,zoneId);
            if(!checkExistLog){//tao moi
               StringBuffer query = new StringBuffer();
                query.append("INSERT INTO gamedata (userId,zoneId, win, lost, expr,updateDate, totalplay, typeMoney) ");
                query.append("VALUES (? , ? , ? , ?, ?, ? ,? , ?)");

                stm = this.conn.prepareStatement(query.toString(), 1);
               
                stm.setLong(1, userID);
                stm.setInt(2, zoneId);
                stm.setInt(3, win);
                stm.setInt(4, lost);
                stm.setInt(5, exp);
                stm.setString(6, now);
                stm.setInt(7, 1);
                stm.setInt(8, typeMoney);
                stm.executeUpdate();
                rs = stm.getGeneratedKeys();
                if (rs.next()) {
                    long l = rs.getInt(1);
                    logRs = l;

                } 
            }else{
                StringBuilder queryUpdate = new StringBuilder();
                queryUpdate.append("update gamedata set win = win + ?, ");
                queryUpdate.append("lost = lost + ?, expr = expr + ?,  ");
                queryUpdate.append("updateDate = ?, totalplay = totalplay + 1 ");
                queryUpdate.append("WHERE userId = ? and zoneId = ?");
                stm = this.conn.prepareStatement(queryUpdate.toString());
                stm.setInt(1, win);
                stm.setInt(2, lost);
                stm.setInt(3, exp);
                stm.setString(4, now);
                stm.setLong(5, userID);
                stm.setInt(6, zoneId);
                stm.executeUpdate();
            }

        } finally {
            if (this.conn != null) {
                this.conn.close();
            }

            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }

        }
        return logRs;
    }

    public boolean checkExistLog(long userID, int zoneId) throws Exception {
        
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean result = false;
        try {
           
            //query.append("select * from logvasc where userID =? ORDER BY id desc limit ").append(limit);
            String query = "select * from gamedata where userId =? AND zoneId = ? ORDER BY id desc limit 1" ;

            stm = this.conn.prepareStatement(query);
            stm.setLong(1, userID);
            stm.setInt(2, zoneId);
            rs = stm.executeQuery();
            while (rs.next()) {
                        result = true;
            }
                
        } finally {
            

            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }

        }
        return result;
    }

    public static void main(String[] args) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        try {
         
            LogGameDataDB logev = new LogGameDataDB();
            logev.insertLogGameData(103, 10015,true,0,1);
            
            //loge = logev.getLogVasc(312855, 1);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(LogvascDB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
