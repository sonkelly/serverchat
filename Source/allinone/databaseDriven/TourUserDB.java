package allinone.databaseDriven;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import allinone.data.ItemEntity;
import allinone.data.ItemOrderEntity;
import allinone.data.UserEntity;
import java.sql.PreparedStatement;
import vn.game.db.DBException;

/**
 *
 * @author mcb
 */
public class TourUserDB {

    public static long userTourIsExist(long uid, int tID) {
        long result = 0;
        String query = "SELECT uid FROM autotourBida_user WHERE uid=? and tourID=?;";
        Connection con = DBAdmin.instance().getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.prepareStatement(query);
            st.setLong(1, uid);
            st.setInt(2, tID);
            rs = st.executeQuery();
            if (rs != null && rs.next()) {
                result = rs.getLong("uid");
            }

        } catch (SQLException e) {

        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {

            }
        }

        return result;
    }

    public static void main(String[] args) throws Exception {

    }

}
