/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.databaseDriven;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


import allinone.friends.data.friendSumEntity;
import java.sql.PreparedStatement;

import org.slf4j.Logger;
import vn.game.common.LoggerContext;

/**
 *
 * @author mac
 */
public class FriendSumDB {
private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(FriendSumDB.class);
    public friendSumEntity getRecord(long uid) throws SQLException {
        Connection con = null;
        con = DBVip.instance().getConnection();
        StringBuilder query = new StringBuilder();
        query.append("select * from friends_sum where uid= ?");
        PreparedStatement st = null;
        ResultSet rs = null;
        friendSumEntity fSumEntity = new friendSumEntity();
        try {
            st = con.prepareStatement(query.toString());
            st.setLong(1, uid);

            System.out.println("uid:" + uid);
            rs = st.executeQuery();

            if (rs.first()) {
                 System.out.println("da co fSumEntity:" + uid);
                // while (rs.next()) {
                fSumEntity = new friendSumEntity();
                fSumEntity.id = rs.getLong("id");
                fSumEntity.uid = rs.getLong("uid");
                fSumEntity.totalRequest = rs.getInt("totalRequest");
                fSumEntity.totalFriend = rs.getInt("totalFriend");
                fSumEntity.totalReject = rs.getInt("totalReject");
                //}

            } else {//insert record
                System.out.println("insert sum friend:" + uid);
                fSumEntity = this.insert(uid, 0);
            }
        } catch (Exception e) {
            System.out.println("error2:" + e.getMessage());
        } finally {

            if (con != null) {
                con.close();// close init connection
            }
            if (st != null) {
                st.close();// close init connection
            }
            if (rs != null) {
                rs.close();// close init connection
            }
        }
        return fSumEntity;
    }

    public friendSumEntity insert(long uid, int totalRequest) throws SQLException {
        Connection con = null;
        con = DBVip.instance().getConnection();
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO friends_sum (uid, totalRequest, totalFriend, totalReject) ");
        query.append("VALUES (? , ? , ?, ?)");
        PreparedStatement st = null;
        ResultSet rs = null;
        friendSumEntity fSumEntiry = new friendSumEntity();

        try {
            st = con.prepareStatement(query.toString(), 1);
            st.setLong(1, uid);
            st.setInt(2, totalRequest);
            st.setInt(3, 0);
            st.setInt(4, 0);

            st.executeUpdate();
            rs = st.getGeneratedKeys();
            if (rs.next()) {
                long id = rs.getInt(1);
                fSumEntiry.id = id;
                fSumEntiry.uid = uid;
                fSumEntiry.totalRequest = totalRequest;
                return fSumEntiry;//ok

            }
        } catch (Exception e) {
            System.out.println("error:" + e.getMessage());
        } finally {

            if (con != null) {
                con.close();// close init connection
            }
            if (st != null) {
                st.close();// close init connection
            }
            if (rs != null) {
                rs.close();// close init connection
            }
        }
        return fSumEntiry;
    }

    public void update(long uid, int totalRequest) throws SQLException {
        mLog.debug("update sum friend totalRequest:"+totalRequest);
        Connection con = null;
        con = DBVip.instance().getConnection();
        StringBuilder query = new StringBuilder();
        query.append("update friends_sum set totalRequest = ? where uid = ?");
        PreparedStatement st = null;
        ResultSet rs = null;
        //friendSumEntity fSumEntity = null;
        try {
            st = con.prepareStatement(query.toString());
            st.setInt(1, totalRequest);
            st.setLong(2, uid);
            st.executeUpdate();

        } finally {

            if (con != null) {
                con.close();// close init connection
            }
            if (st != null) {
                st.close();// close init connection
            }
            if (rs != null) {
                rs.close();// close init connection
            }
        }
        //return fSumEntity;
    }
    public void updateTotalFriend(long uid, int totalFriend) throws SQLException {
        Connection con = null;
        con = DBVip.instance().getConnection();
        StringBuilder query = new StringBuilder();
        query.append("update friends_sum set totalFriend = ? where uid = ?");
        PreparedStatement st = null;
        ResultSet rs = null;
        //friendSumEntity fSumEntity = null;
        try {
            st = con.prepareStatement(query.toString());
            st.setInt(1, totalFriend);
            st.setLong(2, uid);
            st.executeUpdate();

        } finally {

            if (con != null) {
                con.close();// close init connection
            }
            if (st != null) {
                st.close();// close init connection
            }
            if (rs != null) {
                rs.close();// close init connection
            }
        }
        //return fSumEntity;
    }
}
