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
public class FriendDB {

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
            = LoggerContext.getLoggerFactory().getLogger(FriendDB.class);

    public FriendDB() {

    }

    public FriendDB(Connection conn) {
        this.conn = conn;
    }

    public int addFriend(long uid, long friendId, String isRealMoney) throws Exception {
        int result = 0;

        int status = -1;
        try {
            status = this.isFriend(uid, friendId);
        } catch (SQLException ex) {
            System.out.println("error:" + ex.getMessage());
        }
        System.out.println(uid + "uid status222:" + status + " friendId:" + friendId);
        switch (status) {
            case STATUS_EMPTY_RECORD:
                Connection con = null;
                PreparedStatement stm = null;
                ResultSet rs = null;
                try {
                    //get userinfo
                    UserDB userDb = new UserDB();
                    UserEntity userInfo = userDb.getUserInfo(uid, isRealMoney);
                    UserEntity userFriend = userDb.getUserInfo(friendId, isRealMoney);
                    con = DBVip.instance().getConnection();

                    StringBuffer query = new StringBuffer();
                    query.append("INSERT INTO friends (uid, friendId, requestDate,status,fusername, fviewname, favar,fcountry, fcity, uidRequest,username, viewname) ");
                    query.append("VALUES (? , ? , ?, ? , ? , ? , ?, ? , ? , ?,? , ?)");
                    stm = con.prepareStatement(query.toString(), 1);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    String now = dateFormat.format(date);
                    stm.setLong(1, uid);
                    stm.setLong(2, friendId);
                    stm.setString(3, now);
                    stm.setInt(4, STATUS_FRIEND_REQUESTING);//request
                    stm.setString(5, userFriend.mUsername);
                    stm.setString(6, userFriend.viewName);
                    stm.setString(7, userFriend.avatarID);
                    stm.setString(8, userFriend.usrInfoEntity != null ? userFriend.usrInfoEntity.countryId : "");
                    stm.setString(9, userFriend.usrInfoEntity != null ? userFriend.usrInfoEntity.cityId : "");
                    stm.setLong(10, uid);
                    stm.setString(11, userInfo.mUsername);
                    stm.setString(12, userInfo.viewName);
                    stm.executeUpdate();
                    rs = stm.getGeneratedKeys();
                    if (rs.next()) {
                        rs.getInt(1);
                        System.out.println(" vao day");
                        //log count request friend
                        FriendSumDB fSumObj = new FriendSumDB();
                        friendSumEntity fsumEntity = fSumObj.getRecord(friendId);
                        if (fsumEntity != null) {
                            fSumObj.update(friendId, fsumEntity.totalRequest + 1);
                        }
                        //end log count request friend

                        return STATUS_FRIEND_REQUEST;//ok

                    }

                } catch (Exception e) {
                    mLog.debug("error:" + e.getMessage());
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
                break;
            case STATUS_FRIEND_NOT_APPROVED:
                //update request
                System.out.println("STATUS_FRIEND_REQUEST");
                this.updateRequestFriend(STATUS_FRIEND_REQUESTING, uid, friendId, isRealMoney);
                //this.updateRequestFriend(STATUS_FRIEND_REQUESTING, friendId, uid, isRealMoney);
                result = STATUS_FRIEND_NOT_APPROVED;//da la friend roi
                break;
            case STATUS_FRIEND_REQUESTING:
                result = STATUS_FRIEND_REQUESTING;//da la friend roi
                break;
            default:
                result = STATUS_FRIENDED;//da la friend roi
                break;
        }
        return result;
    }

    public int addFriendByAuto(long uid, long friendId, String isRealMoney, int isApproved) throws Exception {
        int result = 0;

        int status = -1;
        try {
            status = this.isFriend(uid, friendId);
        } catch (SQLException ex) {
            System.out.println("error:" + ex.getMessage());
        }
        System.out.println("status222:" + status);
        if (status == STATUS_EMPTY_RECORD) {

            Connection con = null;

            PreparedStatement stm = null;
            ResultSet rs = null;
            try {
                //get userinfo 
                UserDB userDb = new UserDB();
                UserEntity userInfo = userDb.getUserInfo(uid, isRealMoney);
                UserEntity userFriend = userDb.getUserInfo(friendId, isRealMoney);
                con = DBVip.instance().getConnection();

                StringBuffer query = new StringBuffer();
                query.append("INSERT INTO friends (uid, friendId, requestDate,status,fusername, fviewname, favar,fcountry, fcity,username,viewname) ");
                query.append("VALUES (? , ? , ?, ? , ? , ? , ?, ? , ?, ? , ?)");
                stm = con.prepareStatement(query.toString(), 1);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String now = dateFormat.format(date);
                stm.setLong(1, uid);
                stm.setLong(2, friendId);
                stm.setString(3, now);
                stm.setInt(4, isApproved);//request
                stm.setString(5, userFriend.mUsername);
                stm.setString(6, userFriend.viewName);
                stm.setString(7, userFriend.avatarID);
                stm.setString(8, userFriend.usrInfoEntity != null ? userFriend.usrInfoEntity.countryId : "");
                stm.setString(9, userFriend.usrInfoEntity != null ? userFriend.usrInfoEntity.cityId : "");
                stm.setString(10, userInfo.mUsername);
                stm.setString(11, userInfo.viewName);
                stm.executeUpdate();
                rs = stm.getGeneratedKeys();
                if (rs.next()) {
                    rs.getInt(1);
                    //System.out.println(" vao day");
                    //log count request friend
                    FriendSumDB fSumObj = new FriendSumDB();
                    friendSumEntity fsumEntity = fSumObj.getRecord(uid);
                    if (fsumEntity != null) {
                        fSumObj.update(uid, fsumEntity.totalRequest + 1);
                    }
                    //end log count request friend
                    return STATUS_FRIEND_REQUEST;//ok

                }

            } catch (Exception e) {
                mLog.debug("error:" + e.getMessage());
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
        } else if (status == STATUS_FRIEND_NOT_APPROVED) {//update request
            System.out.println("STATUS_FRIEND_REQUEST");
            this.updateRequestFriend(STATUS_FRIEND_REQUESTING, uid, friendId, isRealMoney);
            result = STATUS_FRIEND_NOT_APPROVED;//da la friend roi
        } else if (status == STATUS_FRIEND_REQUESTING) {
            result = STATUS_FRIEND_REQUESTING;//da la friend roi
        } else {
            result = STATUS_FRIENDED;//da la friend roi
        }
        return result;
    }

    public void removeFriend(long currUID, long friendUID)
            throws Exception {

        String query = "{call uspDeleteFriend(?,?)}";

        Connection conn = DBPoolConnection.getConnection();
        try {
            CallableStatement cs = conn.prepareCall(query);
            cs.setLong(USER_ID_PARAM, currUID);
            cs.setLong(FRIEND_ID_PARAM, friendUID);
            cs.execute();
        } finally {
            conn.close();
        }
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

    public List<UserEntity> getRequestFriends(long uid, boolean isOnline, int pageIndex, int status) throws SQLException {

        Vector<UserEntity> users = new Vector<UserEntity>();
        if (pageIndex <= 0) {
            pageIndex = 1;
        }
        int items_per_page = 20;
        // build query
        int offset = (pageIndex - 1) * items_per_page;
        String limit = offset + "," + items_per_page;

        String query = "select * from friends where friendId = ? and status = ? limit " + limit;

        Connection con = null;
        con = DBVip.instance().getConnection();

        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = con.prepareStatement(query);
            stm.setLong(1, uid);
            //if (status != 0) {
            stm.setInt(2, STATUS_FRIEND_REQUESTING);
            //}
            rs = stm.executeQuery();

            rs = stm.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    UserEntity res = new UserEntity();

                    res.mUid = rs.getLong("uid");

                    res.mUsername = rs.getString("username");
                    res.viewName = rs.getString("viewname");
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

    public int getNumRequestFriends(long userId) throws Exception {
        int ret = 0;
        String query = "{ call uspGetNumRequestFriends(?) }";

        Connection con = null;

        if (this.conn == null) {
            con = DBPoolConnection.getConnection();
        } else {
            con = this.conn;
        }
        ResultSet rs = null;
        CallableStatement cs = null;
        try {
            cs = con.prepareCall(query);
            cs.setLong(USER_ID_PARAM, userId);
            rs = cs.executeQuery();
            if (rs != null && rs.next()) {
                ret = rs.getInt("count");
                rs.close();
            }

        } finally {
            if (con != null) {
                con.close();
            }

            if (rs != null) {
                rs.close();
            }

            if (cs != null) {
                cs.close();
            }
        }

        return ret;

    }

    public List<UserEntity> findFriends(boolean isMale, int countryId, int cityId, String name, int pageIndex) throws SQLException {
        List<UserEntity> lstUsers = new ArrayList<UserEntity>();
        if (pageIndex <= 0) {
            pageIndex = 1;
        }
        int items_per_page = 20;
        // build query
        int offset = (pageIndex - 1) * items_per_page;
        String limit = offset + "," + items_per_page;
        String query = "select uid,viewname,sex,avatar,groupId from users where viewname LIKE '%" + name + "%' limit " + limit;

        Connection con = DBGame.instance().getConnection();
        PreparedStatement cs = null;
        ResultSet rs = null;
        try {
            cs = con.prepareStatement(query.toString());
            rs = cs.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    UserEntity res = new UserEntity();
                    res.mUid = rs.getLong("uid");
                    res.viewName = rs.getString("viewname");
                    //System.out.println(res.mUid+"res.mUid - res.viewName:"+res.viewName);
                    res.mIsMale = rs.getBoolean("sex");
                    res.avatarID = rs.getString("avatar");
                    lstUsers.add(res);

                }

            }

        } finally {
            if (con != null) {
                con.close();
            }

            if (rs != null) {
                rs.close();
            }

            if (cs != null) {
                cs.close();
            }
        }
        return lstUsers;
    }

    public int isFriend(long uid, long friendId) throws SQLException {
        //0 request, 1 friend,2 request, -1 remove friend
        int status = STATUS_EMPTY_RECORD;

        Connection con = null;
        con = DBVip.instance().getConnection();
        StringBuilder query = new StringBuilder();
        query.append("select * from friends where uid= ? and friendId = ?");
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.prepareStatement(query.toString());
            st.setLong(1, uid);
            st.setLong(2, friendId);
            //System.out.println("uid:" + uid + " friendId:" + friendId);
            rs = st.executeQuery();

            if (rs != null) {
                if (rs.first()) {
                    status = rs.getInt("status");
                }

            }
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
        return status;
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
                    if(fsumEntity.totalRequest > 0){
                        fSumObj.update(uid, fsumEntity.totalRequest - 1);
                    }
                }
//truong dhop dong ky ket ban thi insert doi tuong nguoi gui cung co list ban 
                if (status == STATUS_FRIENDED) {
                    try {
                        this.addFriendByAuto(friendId, uid, isRealMoney, STATUS_FRIENDED);
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
                    //mLog.debug("exist sum friend");
                    fSumObj.update(uid, fsumEntity.totalRequest - 1);
                }
//truong dhop dong ky ket ban thi insert doi tuong nguoi gui cung co list ban
                //mLog.debug("status accept friend:" + status);
                if (status == STATUS_FRIENDED) {
                    try {//tu dong them row khi co nguoi
                        this.addFriendByAuto(uid, friendId, isRealMoney, STATUS_FRIENDED);
                        fSumObj.updateTotalFriend(uid, fsumEntity.totalFriend + 1);
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
        String query = "select * from friends where uid = ? or friendId = ? limit " + limit;
        if (status == 0) {
            query = "select * from friends where uid = ? or friendId = ? limit " + limit;
        } else {
            query = "select * from friends where uid = ? or friendId = ? and status = ? limit " + limit;

        }

        Connection con = null;
        con = DBVip.instance().getConnection();

        PreparedStatement stm = null;
        ResultSet rs = null;
        try {

            stm = con.prepareStatement(query);
            stm.setLong(1, uid);
            stm.setLong(2, uid);
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
