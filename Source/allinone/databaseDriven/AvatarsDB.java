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

import java.util.ArrayList;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;

import allinone.data.AvatarEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;

/**
 *
 * @author Zeple
 */
@SuppressWarnings("unused")
public class AvatarsDB {

    private static final Logger mLog = LoggerContext.getLoggerFactory()
            .getLogger(AvatarsDB.class);

    public AvatarsDB() {
    }

    public ArrayList<AvatarEntity> getAvatars() throws Exception {
        Connection con = null;

        con = DBPoolConnectionAdmin.getConnection();

        PreparedStatement stm = null;
        ResultSet rs = null;
        ArrayList result = new ArrayList();
        try {

            StringBuffer query = new StringBuffer();
            query.append("select * from avatars where active =?");

            mLog.debug("logvas: ", query.toString());
            stm = con.prepareStatement(query.toString());
            stm.setLong(1, 1);

            rs = stm.executeQuery();
            while (rs.next()) {

                AvatarEntity ne = new AvatarEntity();
                ne.id = rs.getInt("id");
                ne.avatar = rs.getString("avatar");

                result.add(ne);
            }

        } finally {
            if (con != null) {
                con.close();
            }

            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }

        }
        return result;
    }

    public void updateAvatar(long uid, String imageId) throws Exception {
        Connection conn = null;

        conn = DBGame.instance().getConnection();
        PreparedStatement stm = null;
        StringBuffer query = new StringBuffer();
        try {
            query.append("UPDATE users SET avatar=?  WHERE uid=?");

            stm = conn.prepareStatement(query.toString());
            stm.setString(1, imageId);
            stm.setLong(2, uid);
            stm.executeUpdate();
        } finally {
            if (conn != null) {
                conn.close();
            }

            if (stm != null) {
                stm.close();
            }

        }
    }

    public static void main(String[] args) {

        try {
            //        try {
//            AvatarEntity loge = new AvatarEntity();
//            AvatarsDB logev = new AvatarsDB();
//            ArrayList<AvatarEntity> loges = logev.getAvatars();
//            for (AvatarEntity ett : loges) {
//
//                System.out.println("log:" + ett.avatar);
//            }
//            //loge = logev.getLogVasc(312855, 1);
//        } catch (Exception ex) {
//            java.util.logging.Logger.getLogger(LogvascDB.class.getName()).log(Level.SEVERE, null, ex);
//        }
            AvatarsDB logev = new AvatarsDB();
            logev.updateAvatar(1, "ava-1.jpg");
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(AvatarsDB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
