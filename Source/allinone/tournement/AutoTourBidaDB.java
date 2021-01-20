/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.tournement;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import allinone.data.GioiThieuEntity;
import static allinone.data.SimpleTable.mLog;
import allinone.data.SocialFriendEntity;
import allinone.data.UserEntity;
import allinone.databaseDriven.DBAdmin;
import allinone.friends.data.friendSumEntity;
import allinone.protocol.messages.json.AddFriendJSON;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import org.slf4j.Logger;
import vn.game.common.LoggerContext;

/**
 *
 * @author mac
 */
public class AutoTourBidaDB {

    public AutoTourBidaDB() {

    }

    public ArrayList<TournementEntity> getTourRuning() throws SQLException {
        ArrayList<TournementEntity> tours = new ArrayList<TournementEntity>();

        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {

            conn = DBAdmin.instance().getConnection();

            stm = conn.prepareStatement("SELECT * FROM  `autotourBida` WHERE (`status` = ? || `status` = ?)");
            stm.setInt(1, TourStatus.TOUR_WAIT);
            stm.setInt(2, TourStatus.TOUR_STARTING);
            rs = stm.executeQuery();
            while (rs.next()) {

                int id = rs.getInt("id");

                String name = rs.getString("name");
                int gameId = rs.getInt("gameId");
                int creatorId = rs.getInt("creatorId");
                int money_coc = rs.getInt("money_coc");
                int money_cuoc = rs.getInt("money_cuoc");
                int statusID = rs.getInt("status");
                int templateId = rs.getInt("templateId");

                Date date = rs.getDate("createdtime");

                TournementEntity tour = new TournementEntity(id, date, date, name, money_cuoc, gameId, creatorId, statusID, templateId,0);
                tours.add(tour);

            }

            // return result;
        } catch (SQLException e1) {

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
        return tours;

    }

    public ArrayList<TournementEntity> getTours(int status) throws SQLException {
        ArrayList<TournementEntity> tours = new ArrayList<TournementEntity>();

        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {

            conn = DBAdmin.instance().getConnection();

            stm = conn.prepareStatement("SELECT * FROM  `autotourBida` WHERE `status` = ?");
            stm.setInt(1, status);
            rs = stm.executeQuery();
            while (rs.next()) {

                int id = rs.getInt("id");

                String name = rs.getString("name");
                int gameId = rs.getInt("gameId");
                int creatorId = rs.getInt("creatorId");
                int money_coc = rs.getInt("money_coc");
                int money_cuoc = rs.getInt("money_cuoc");
                int statusID = rs.getInt("status");
                int templateId = rs.getInt("templateId");

                Date date = rs.getDate("createdtime");

                TournementEntity tour = new TournementEntity(id, date, date, name, money_cuoc, gameId, creatorId, statusID, templateId,0);
                tours.add(tour);

            }

            // return result;
        } catch (SQLException e1) {

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
        return tours;

    }
    //get tour one
    public TournementEntity getTourById(int id)  {
        TournementEntity tour = null;

        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {

            conn = DBAdmin.instance().getConnection();

            stm = conn.prepareStatement("SELECT * FROM  `autotourBida` WHERE `id` = ?");
            stm.setLong(1, id);
            rs = stm.executeQuery();
           if (rs.next()) {


                String name = rs.getString("name");
                int gameId = rs.getInt("gameId");
                int creatorId = rs.getInt("creatorId");
                int money_coc = rs.getInt("money_coc");
                int money_cuoc = rs.getInt("money_cuoc");
                int statusID = rs.getInt("status");
                int templateId = rs.getInt("templateId");

                Date date = rs.getDate("createdtime");

                tour = new TournementEntity(id, date, date, name, money_cuoc, gameId, creatorId, statusID, templateId,0);
              

            }

            // return result;
        } catch (SQLException e1 ) {

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
        return tour;

    }
    public long createTour(TourTemplateEntity tourTmp) {
        Connection con = null;
        long tourid = 0;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            //get userinfo 

            con = DBAdmin.instance().getConnection();

            StringBuffer query = new StringBuffer();
            query.append("INSERT INTO autotourBida (name, money_coc, money_cuoc,createdtime,moneyType, gameId, creatorId,templateId, status) ");
            query.append("VALUES (? , ? , ?, ? , ? , ? , ?, ? , ? )");
            stm = con.prepareStatement(query.toString(), 1);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String now = dateFormat.format(date);
            stm.setString(1, tourTmp.name);
            stm.setLong(2, tourTmp.money_coc);
            stm.setLong(3, tourTmp.money_cuoc);
            stm.setString(4, now);
            stm.setInt(5, tourTmp.moneyType);//request
            stm.setInt(6, tourTmp.game);//request
            stm.setInt(7, 0);//request
            stm.setInt(8, tourTmp.id);//request
            stm.setInt(9, TourStatus.TOUR_WAIT);//0 khoi tao tour

            stm.executeUpdate();
            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                tourid = rs.getInt(1);

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
        return tourid;
    }
}
