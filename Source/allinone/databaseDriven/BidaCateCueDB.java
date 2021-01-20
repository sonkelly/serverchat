/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.databaseDriven;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import allinone.data.GioiThieuEntity;
import allinone.data.SocialFriendEntity;
import allinone.data.UserEntity;
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
 * @author mcb
 */
public class BidaCateCueDB {

    private static final String USER_ID_PARAM = "uid";
    private static final String USER_NAME_PARAM = "userName";
    private static final String FRIEND_ID_PARAM = "friendId";
    private static final String ADD_PARAM = "add";
    private static final String ISMALE_PARAM = "isMale";
    private static final String CITY_ID_PARAM = "cityId";
    private static final String JOB_ID_PARAM = "jobId";
    private static final String FROM_YEAR_PARAM = "fromYear";
    private static final String TO_YEAR_PARAM = "toYear";
    private static final String CHARACTER_ID_PARAM = "characterId";
    private static final String HAS_AVATAR_PARAM = "hasAvatar";
    private static final String IS_ONLINE_PARAM = "isonline";
    private static final String FRIEND_NAME_PARAM = "friendName";
    private static final String PHONE_OR_EMAIL_PARAM = "phoneOrEmail";
    private static final String DEVICE_PARAM = "device";
    private static final String PARTNER_ID_PARAM = "partnerId";
    private static final String POSITION_GIOI_THIEU_PARAM = "positionGioiThieu";
    private static final String REF_GIOI_THIEU_PARAM = "refGioithieuId";

    public static final int STATUS_EMPTY_RECORD = 0;//empty record
    public static final int STATUS_FRIEND_REQUESTING = 2;// request friend pending
    public static final int STATUS_FRIEND_REQUEST = 3;// request friend;
    public static final int STATUS_FRIENDED = 1;// friended
    public static final int STATUS_FRIEND_NOT_APPROVED = -1;
    public static final int STATUS_FRIEND_REMOVE = -2;

    private Connection conn;
    private final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(BidaCateCueDB.class);

    public BidaCateCueDB() {

    }

    public BidaCateCueDB(Connection conn) {
        this.conn = conn;
    }

    public List<UserEntity> getSocialFriends(long userId) throws SQLException {
        List<UserEntity> res = new ArrayList<UserEntity>();

        String query = "SELECT * from friends WHERE uid = ?";
        Connection con = DBPoolConnection.getConnection();
        PreparedStatement stm = null;
        //CallableStatement cs = null;
        ResultSet rs = null;
        try {

            //CallableStatement cs = con.prepareCall(query);
            stm = con.prepareStatement(query);
            stm.setLong(1, userId);
            //ResultSet rs = cs.executeQuery();
            rs = stm.executeQuery();
            while (rs.next()) {
                long friendId = rs.getLong("friendId");
                String fname = rs.getString("fName");
                long fcash = rs.getLong("fCash");
                boolean isOnline = rs.getBoolean("fOnline");
                long avFileId = rs.getLong("avatarFileId");
                UserEntity entity = new UserEntity();
                entity.mUsername = fname;
                entity.mUid = friendId;
                entity.money = fcash;
                entity.isOnline = isOnline;
                entity.avFileId = avFileId;
                res.add(entity);
            }

            //cs.close();
        } finally {
            if (con != null) {
                con.close();
            }

            if (rs != null) {
                rs.close();
            }

            if (stm != null) {
                stm.close();
            }
        }
        return res;
    }


    public int updateRequestFriend(int status, long uid, long friendId, String isRealMoney) throws SQLException {
        String query = "update friends set status = ? where uid = ? and friendId = ? ";
        Connection con = null;
        PreparedStatement stm = null;

        try {

            con = DBVip.instance().getConnection();
            stm = con.prepareStatement(query);
            stm.setInt(1, status);
            stm.setLong(2, uid);
            stm.setLong(3, friendId);
            stm.executeUpdate();
            //uddate sum record
            if (status == STATUS_FRIENDED || status == STATUS_FRIEND_NOT_APPROVED) {
                FriendSumDB fSumObj = new FriendSumDB();
                friendSumEntity fsumEntity = fSumObj.getRecord(uid);
                if (fsumEntity != null) {
                    fSumObj.update(uid, fsumEntity.totalRequest - 1);
                }
//truong dhop dong ky ket ban thi insert doi tuong nguoi gui cung co list ban 
                if (status == STATUS_FRIENDED) {
                    try {
                       // this.addFriendByAuto(friendId, uid, isRealMoney, STATUS_FRIENDED);
                    } catch (Exception ex) {
                        java.util.logging.Logger.getLogger(FriendDB.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //end
            }
            //end update sum record
        } finally {
            if (con != null) {
                con.close();
            }
            if (stm != null) {
                stm.close();
            }

        }
        return status;
    }

    //dong y ket ban
    public int approvedFriend(int status, long uid, long friendId, String isRealMoney) throws SQLException {
        String query = "update friends set status = ? where friendId = ? and uid = ? ";
        Connection con = null;
        PreparedStatement stm = null;

        try {

            con = DBVip.instance().getConnection();
            stm = con.prepareStatement(query);
            stm.setInt(1, status);
            stm.setLong(2, uid);
            stm.setLong(3, friendId);
            stm.executeUpdate();
            //uddate sum record
            if (status == STATUS_FRIENDED || status == STATUS_FRIEND_NOT_APPROVED) {
                FriendSumDB fSumObj = new FriendSumDB();
                friendSumEntity fsumEntity = fSumObj.getRecord(uid);
                if (fsumEntity != null) {
                    fSumObj.update(uid, fsumEntity.totalRequest - 1);
                }
//truong dhop dong ky ket ban thi insert doi tuong nguoi gui cung co list ban 
                if (status == STATUS_FRIENDED) {
                    try {//tu dong them row khi co nguoi
                        //this.addFriendByAuto(uid, friendId, isRealMoney, STATUS_FRIENDED);
                    } catch (Exception ex) {
                        java.util.logging.Logger.getLogger(FriendDB.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //end
            }
            //end update sum record
        } finally {
            if (con != null) {
                con.close();
            }
            if (stm != null) {
                stm.close();
            }

        }
        return status;
    }

    public Vector<UserEntity> getFrientList(long uid, boolean isOnline, int pageIndex, int status) throws SQLException {
        Vector<UserEntity> users = new Vector<UserEntity>();
        if (pageIndex <= 0) {
            pageIndex = 1;
        }
        int items_per_page = 20;
        // build query
        int offset = (pageIndex - 1) * items_per_page;
        String limit = offset + "," + items_per_page;
        String query = "select * from friends where uid = ? limit " + limit;
        if (status == 0) {
            query = "select * from friends where uid = ? limit " + limit;
        } else {
            query = "select * from friends where uid = ? and status = ? limit " + limit;

        }

        Connection con = null;
        con = DBVip.instance().getConnection();

        PreparedStatement stm = null;
        ResultSet rs = null;
        try {

            stm = con.prepareStatement(query);
            stm.setLong(1, uid);
            if (status != 0) {
                stm.setInt(2, status);
            }
            rs = stm.executeQuery();

            rs = stm.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    UserEntity res = new UserEntity();

                    res.mUid = rs.getLong("friendId");

                    res.mUsername = rs.getString("fusername");
                    res.viewName = rs.getString("fviewname");
                    res.avatarID = rs.getString("favar");
                    res.isLogin = rs.getBoolean("isOnline");
                    res.utype = rs.getInt("status");

                    System.out.println("res.mUsername:" + res.mUsername);
                    users.add(res);
                }

            }

        } finally {
            if (con != null) {
                con.close();
            }
            if (stm != null) {
                stm.close();
            }

        }

        return users;
    }

    public static void main(String[] args) throws Exception {
        FriendDB friendObj = new FriendDB();

        //friendObj.addFriend(103, 104, "realmoney");
        //get friend
        //friendObj.getFrientList(103,true,1,0);
        //friendObj.getFrientList(104,true,1,2);
        //find friend 
//        boolean isMale,int countryId, int cityId, String name
        //friendObj.findFriends(true, 1, 1, "", 1);
        //get list friend request
        //friendObj.getRequestFriends(104, true, 1, 2);
        //update friend, accept or not accept
        //friendObj.updateRequestFriend(STATUS_FRIENDED, 104, 103, "realmoney");
        //approved friend
        friendObj.approvedFriend(STATUS_FRIENDED, 104, 103, "realmoney");

    }
}
