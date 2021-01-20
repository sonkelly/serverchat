/*
 * To change this template, choose Tools | Templates
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
import allinone.data.SimplePlayer;
import allinone.data.SimpleTable;
import allinone.data.UserEntity;
import allinone.data.UserInfoEntity;
import allinone.data.ZoneID;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import allinone.data.AIOConstants;
import allinone.data.Phone;
import static allinone.data.ZoneID.BACAY;
import allinone.data.languages.Languages;
import java.util.Random;

/**
 *
 * @author NamNT
 */
@SuppressWarnings("unused")
public class UserDB {

    private static final Logger mLog = LoggerContext.getLoggerFactory()
            .getLogger(UserDB.class);
    private static final String USER_NAME_PARAM = "userName";
    private static final String USER_ID_PARAM = "userId";
    private static final String CITY_ID_PARAM = "cityId";
    private static final String ADDRESS_PARAM = "address";
    private static final String JOB_ID_PARAM = "jobId";
    private static final String BIRTHDAY_PARAM = "birthday";
    private static final String HOBBY_PARAM = "hobby";
    private static final String NICK_SKYPE_PARAM = "nickSkype";
    private static final String NICK_YAHOO_PARAM = "nickYahoo";
    private static final String PHONE_NUMBER_PARAM = "phoneNumber";
    private static final String AVATAR_FILE_ID_PARAM = "avatarFileId";
    private static final String CHARACTER_ID_PARAM = "characterId";
    private static final String SCREEN_PARAM = "screen";
    private static final String MOBILE_VERSION_PARAM = "mobileVersion";
    private static final String IP_PARAM = "ip";
    private static final String DEVICE_ID_PARAM = "deviceId";
    private static final String TOP_USER_PARAM = "topUser";
    private static final String FROM_DATE_PARAM = "fromDate";
    private static final String TO_DATE_PARAM = "toDate";
    private static final String LOG_TYPE_ID_PARAM = "logTypeId";
    private static final String PASSWORD_PARAM = "password";
    private static final String PHONG_ID_PARAM = "phongId";
    private static final String AGE_PARAM = "age";
    private static final String SEX_PARAM = "sex";
    private static final String CHK_PARTNER_SIDE_PARAM = "chkPartnerSide";
    private static final String INTRODUCE_ID_PARAM = "introduceId";
    private static final String IS_MXH_PARAM = "isMxh";
    private static final String SERIAL_PARAM = "serial";
    private static final String DEVICE_TYPE_PARAM = "deviceType";
    private static final String EMAIL_PARAM = "email";
    private static final String PHONE_PARAM = "phone";
    private static final String PARTNER_ID_PARAM = "partnerId";
    private static final String KEY_ID_PARAM = "keyId";
    private static final String COLLECT_INFO_PARAM = "collectInfo";
    private static final String MONEY_PARAM = "money";
    private static final String DT_PARAM = "dt";
    private static final String TYPE_PARAM = "type";
    private static final String FILE_ID_PARAM = "fileId";
    private static final String STATUS_PARAM = "stt";
    private static final String REF_CODE_PARAM = "refCode";
    private static List<UserEntity> lstBotUser;
    public static final boolean isRealMoney = true;
    public static final String MONEY_PLAY = "realmoney";
    public static final int utype_bot_tx = 9;
    public static final int utype_bot_xocdia = 10;
    public static final int utype_xocdia_owner = 15;
    //private Connection conn;true
    //private Connection connGame;

    public UserDB() {
    }

    public UserDB(Connection con) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    public UserDB(Connection con) {
//        this.conn = con;
//    }
    public void clearLogin() throws SQLException {

        String query = "{ call uspClearLogin() }";
        Connection con = DBPoolConnection.getConnection();
        try {
            CallableStatement cs = con.prepareCall(query);
            cs.execute();
            cs.close();
        } finally {
            con.close();
        }

    }

    public void updateGameEvent(long uid, int gameID) throws SQLException {

        String query = "{ call UpdateGameEventUser(?,?) }";
        Connection con = DBPoolConnection.getConnection();
        try {
            CallableStatement cs = con.prepareCall(query);
            cs.clearParameters();
            cs.setLong(1, uid);
            cs.setInt(2, gameID);
            cs.executeUpdate();
            cs.close();
            mLog.debug("Update Event: " + uid + ":" + gameID);
        } finally {
            con.close();
        }

    }

    public void updateGameEventALTP(long uid, long gamePoint) throws SQLException {

        String query = "{ call UpdateGameEventUserForALTP(?,?) }";
        Connection con = DBPoolConnection.getConnection();
        try {
            CallableStatement cs = con.prepareCall(query);
            cs.clearParameters();
            cs.setLong(1, uid);
            cs.setLong(2, gamePoint);
            cs.executeUpdate();
            cs.close();
            mLog.debug("Update Event: " + uid + ":" + gamePoint);
        } finally {
            con.close();
        }

    }

    public String loginEvent(long userId, String version) throws DBException, SQLException {
        String query = "{call uspLoginEvent(?, ?) }";
        // Connection con = DBPoolConnection.getConnection();
        //  String ret = "-1";
//        try {
//            CallableStatement cs = con.prepareCall(query);
//            //cs.registerOutParameter(1, Types.NVARCHAR);
//
//            cs.setLong(1, userId);
//            cs.setString(2, version);
//            ResultSet rs = cs.executeQuery();
//            if (rs != null && rs.next()) {
//                ret = rs.getString("result");
//            }
//            cs.close();
//        } finally {
//            con.close();
//        }
        return "1";
    }

    public int updatePhoneNumber(long userId, String phoneNumber) throws Exception {
        Connection con = DBPoolConnection.getConnection();
        int ret = 0;

        String query = "{?=  call uspPhoneNumber(?,?) }";
        try {
            CallableStatement cs = con.prepareCall(query);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setLong(USER_ID_PARAM, userId);
            cs.setString("phoneNumber", phoneNumber);
            cs.execute();

            ret = cs.getInt(1);
        } finally {
            con.close();
        }
        return ret;
    }

    public int updatePhoneNumberAndUserName(long userId, String phoneNumber, String userName) throws Exception {
        mLog.debug(userId + "  " + phoneNumber + "  " + userName);

        Connection con = DBPoolConnection.getConnection();
        int ret = 0;

        String query = "{?=  call uspPhoneNumberAndUserName(?,?,?)}";
        try {
            CallableStatement cs = con.prepareCall(query);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setLong("userId", userId);
            cs.setString("phoneNumber", phoneNumber);
            cs.setString("userName", userName);
            cs.execute();
            ret = cs.getInt(1);
        } finally {
            con.close();
        }
        return ret;
    }

    public int changePassword(long userId, String oldPassword, String newPassword) throws DBException, SQLException {
        String query = "Update users SET pass = ? WHERE uid = ? AND pass = ?";
        Connection con = null;
        int ret = 0;
        CallableStatement cs = null;
        try {
            con = DBGame.instance().getConnection();
            cs = con.prepareCall(query);
            cs.setString(1, newPassword);
            cs.setLong(2, userId);
            cs.setString(3, oldPassword);
            ret = cs.executeUpdate();

            mLog.debug(" return value " + ret);

        } finally {
            if (con != null) {
                con.close();
            }
            if (cs != null) {
                cs.close();
            }
        }

        return ret;
    }

    public int changeViewName(long userId, String viewname) throws DBException, SQLException {
        String query = "Update users SET viewname = ? WHERE uid = ?";
        Connection con = null;
        int ret = 0;
        PreparedStatement stm = null;
        try {
            con = DBGame.instance().getConnection();
            stm = con.prepareStatement(query);
            stm.setString(1, viewname);
            stm.setLong(2, userId);
            stm.executeUpdate();
            ret = 1;
            mLog.debug(" return value " + ret);

        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return ret;
    }

    public int changeViewNameByFastLogin(long userId, String viewname, String username) throws DBException, SQLException {
        String query = "Update users SET username = ? ,viewname = ? WHERE uid = ?";
        Connection con = null;
        int ret = 0;
        PreparedStatement stm = null;
        try {
            con = DBGame.instance().getConnection();
            stm = con.prepareStatement(query);
            stm.setString(1, username);
            stm.setString(2, viewname);
            stm.setLong(3, userId);
            stm.executeUpdate();
            ret = 1;
            mLog.debug(" return value " + ret);

        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return ret;
    }

    public static void reload() {
        try {
            lstBotUser = getBotUser("realmoney");
        } catch (Exception ex) {
            mLog.error(ex.getMessage());
        }
    }

    public boolean checkBotUser(long userId) {
        /*
         int botSize = lstBotUser.size();
         for (int i = 0; i < botSize; i++) {
         UserEntity entity = lstBotUser.get(i);
         if (entity.mUid == userId) {
         return true;
         }

         }*/

        return false;
    }

    public UserEntity guestLogin(String deviceUid, int partnerId, int refCode, String mobileVersion, int deviceType, String isRealMoney) throws SQLException {
        UserEntity res = null;

        //String query = "{ call uspFastLogin(?, ?, ?, ?, ?, ?) }";
        String query = "select * from ";

        mLog.debug(query + deviceUid + partnerId + refCode + mobileVersion);

        //Connection con = DBPoolConnection.getConnection();
        Connection con = null;
        ResultSet rs = null;
        CallableStatement cs = null;

        try {
            con = DBGame.instance().getConnection();
            cs = con.prepareCall(query);
            cs.setString(1, deviceUid);
            cs.setString(2, MD5.toMD5(deviceUid));
            cs.setInt(3, partnerId);
            cs.setInt(4, refCode);
            cs.setString(5, mobileVersion);
            cs.setInt(6, deviceType);

            rs = cs.executeQuery();

            if (rs != null && rs.next()) {
                res = new UserEntity();

                res.mUid = rs.getLong("UserID");
                res.mUsername = rs.getString("Name");
                res.mPassword = rs.getString("password");
                res.lastLogin = rs.getTimestamp("lastLogin");
                res.mIsMale = rs.getBoolean("sex");
                res.avatarID = rs.getString("avatar");
                res.level = rs.getInt("Level");
                res.money = rs.getLong(isRealMoney);

                res.playsNumber = rs.getInt("WonPlaysNumber");
                res.cellPhone = rs.getString("PhoneNumber");
                res.isActive = rs.getBoolean("isActive");
                res.experience = rs.getInt("experience");
                res.isOnline = rs.getBoolean("isOnline");
                res.vipId = rs.getInt("vipId");
                res.avFileId = rs.getLong("avatarFileId");
                res.biaFileId = rs.getLong("biaFileId");
                res.partnerId = rs.getInt("partnerId");
                res.stt = rs.getString("status");
                res.vipName = "Vip " + Integer.toString(res.vipId);
                res.refCode = rs.getInt("refCode");
                res.isLock = rs.getInt("isLock");

            }

        } catch (Throwable ex) {

            mLog.error(ex.getMessage(), ex);
        } finally {

            if (con != null) {
                con.close();
            }
            if (cs != null) {
                cs.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return res;
    }

    public UserEntity socialLogin(String socialId, int partnerId, String refCode, String mobileVersion, int deviceType, String ip) throws SQLException {
        UserEntity res = null;

        int loginType = 2; // 1 face, 2 google

        if (socialId.contains("gmail.com")) {
            loginType = 3;
        }

        String query = "{ call uspSocialLogin(?, ?, ?, ?, ?, ?, ?, ?) }";

        mLog.debug(query + socialId + partnerId + refCode + mobileVersion);

        Connection con = DBPoolConnection.getConnection();
        ResultSet rs = null;
        CallableStatement cs = null;
        try {

            cs = con.prepareCall(query);
            cs.setString(1, socialId);
            cs.setString(2, MD5.toMD5("social" + socialId));
            cs.setInt(3, partnerId);
            cs.setString(4, refCode);
            cs.setString(5, mobileVersion);
            cs.setInt(6, loginType);
            cs.setInt(7, deviceType);
            cs.setString(8, ip);

            rs = cs.executeQuery();

            if (rs != null && rs.next()) {
                res = new UserEntity();

                res.mUid = rs.getLong("UserID");
                res.mUsername = rs.getString("Name");
                res.name = rs.getString("fullName");
                res.mPassword = rs.getString("password");
                res.lastLogin = rs.getTimestamp("lastLogin");
                res.mIsMale = rs.getBoolean("sex");
                res.avatarID = rs.getString("avatar");
                res.level = rs.getInt("Level");
                res.money = rs.getLong("Cash");
                res.playsNumber = rs.getInt("WonPlaysNumber");
                res.cellPhone = rs.getString("PhoneNumber");
                res.isActive = rs.getBoolean("isActive");
                res.experience = rs.getInt("experience");
                res.isOnline = rs.getBoolean("isOnline");
                res.vipId = rs.getInt("vipId");
                res.avFileId = rs.getLong("avatarFileId");
                res.biaFileId = rs.getLong("biaFileId");
                res.partnerId = rs.getInt("partnerId");
                res.stt = rs.getString("status");
                res.vipName = "Vip " + Integer.toString(res.vipId);
                res.refCode = rs.getInt("refCode");
                res.isLock = rs.getInt("isLock");

                rs.close();

            }
            cs.clearParameters();
            cs.close();

        } catch (Throwable ex) {

            con.close();
            mLog.error(ex.getMessage(), ex);
        } finally {
            con.close();
        }
        return res;
    }

    //add by zep
    public UserEntity fbLogin(int uid, String isRealMoney) throws SQLException {
        UserEntity res = null;

        //String query = "{ call uspSocialLogin(?, ?, ?, ?, ?, ?, ?, ?) }";
        String query = "SELECT * FROM users WHERE uid=? ";

        mLog.debug(query + uid);

        Connection con = DBPoolConnectionGame.getConnection();
        ResultSet rs = null;
        CallableStatement cs = null;
        PreparedStatement stm = null;
        try {

            //cs = con.prepareCall(query);
            stm = con.prepareStatement(query);
            stm.setInt(1, uid);

            rs = stm.executeQuery();

            if (rs != null && rs.next()) {
                res = new UserEntity();

                res = new UserEntity();
                res.mUid = rs.getLong("uid");
                res.mUsername = rs.getString("username");
                res.viewName = rs.getString("viewname");
                res.mPassword = rs.getString("pass");
                res.lastLogin = rs.getTimestamp("lastDateLogin");
                res.mIsMale = rs.getBoolean("sex");
                res.avatarID = rs.getString("avatar");
                res.level = rs.getInt("level");
                res.money = rs.getLong("money");
                res.realmoney = rs.getLong("realmoney");
                res.playsNumber = rs.getInt("total");
                res.cellPhone = "xxxx";
                res.isActive = rs.getBoolean("active");
                res.experience = rs.getInt("exp");
                res.isOnline = true;
                res.vipId = rs.getInt("level");
                res.groupId = rs.getInt("groupId");
                //res.partnerId = rs.getInt("partnerId");
                res.partnerId = 1;
                res.stt = rs.getString("status");
                //res.isLock = rs.getInt("isLock");
                if (rs.getInt("utype") == 6) {
                    res.isLock = 1;
                }

                //res.vipName = "Vip " + Integer.toString(res.vipId);
                res.vipName = "Vip ";
                res.utype = rs.getInt("utype");
                res.mail = rs.getString("email");

            }

        } catch (Throwable ex) {

            mLog.error(ex.getMessage(), ex);
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

    //end by zep
    public UserEntity flashLogin(String userName, String password) throws SQLException {

        UserEntity res = null;
        String query = "{ call uspFlashLogin(?, ?) }";
        Connection con = DBPoolConnection.getConnection();
        ResultSet rs = null;
        CallableStatement cs = null;
        try {

            cs = con.prepareCall(query);
            cs.clearParameters();

            cs.setString(1, userName);
            cs.setString(2, password);

            rs = cs.executeQuery();

            if (rs != null && rs.next()) {
                res = new UserEntity();
                //res.mPassword = rs.getString("Password");

                res.mUid = rs.getLong("UserID");

                res.mUsername = rs.getString("Name");
                res.mPassword = rs.getString("password");
//                res.mAge = rs.getInt("Age");

                //res.lastLogin = rs.getDatetime("LastTime");
                res.lastLogin = rs.getTimestamp("lastLogin");

//                res.mIsMale = rs.getInt("Sex")==1;
//                res.lastMatch=rs.getLong("lastMatch");
                //res.avatarID = rs.getInt("AvatarID");
                res.level = rs.getInt("Level");
                res.money = rs.getLong("Cash");
                res.playsNumber = rs.getInt("WonPlaysNumber");
//                res.avatarF = rs.getString("avatarF");
//                res.avatarM = rs.getString("avatarM");
                res.cellPhone = rs.getString("PhoneNumber");
                res.experience = rs.getInt("experience");
//                res.avatarVersion = rs.getString("avatarVersion");
                res.isOnline = rs.getBoolean("isOnline");
                res.vipId = rs.getInt("vipId");
                res.vipName = "Vip " + Integer.toString(res.vipId);
                rs.close();

            }

            cs.clearParameters();
            cs.close();

        } catch (Throwable ex) {
            con.close();
            con = DBPoolConnection.getConnection();
            cs = con.prepareCall(query);
//            cs.clearParameters();

            cs.setString(1, userName);
            cs.setString(2, password);

            rs = cs.executeQuery();

            if (rs != null && rs.next()) {
                res = new UserEntity();
                //res.mPassword = rs.getString("Password");

                res.mUid = rs.getLong("UserID");

                res.mUsername = rs.getString("Name");
                res.mPassword = rs.getString("password");

//                res.mAge = rs.getInt("Age");
                //res.lastLogin = rs.getDatetime("LastTime");
                res.lastLogin = rs.getTimestamp("lastLogin");

//                res.mIsMale = rs.getInt("Sex")==1;
//                res.lastMatch=rs.getLong("lastMatch");
                //res.avatarID = rs.getInt("AvatarID");
                res.level = rs.getInt("Level");
                res.money = rs.getLong("Cash");
                res.playsNumber = rs.getInt("WonPlaysNumber");
//                res.avatarF = rs.getString("avatarF");
//                res.avatarM = rs.getString("avatarM");
                res.cellPhone = rs.getString("PhoneNumber");
                res.experience = rs.getInt("experience");
//                res.avatarVersion = rs.getString("avatarVersion");

                res.isOnline = rs.getBoolean("isOnline");
                res.vipId = rs.getInt("vipId");
                res.vipName = "Vip " + Integer.toString(res.vipId);
                try {
                    res.refCode = rs.getInt("refCode");
                } catch (Throwable e) {

                }
                rs.close();

            }
            cs.clearParameters();
            cs.close();

        } finally {

            con.close();
        }
        return res;

    }

    public UserEntity login(String userName, String password, String device,
            String mobileVersion, String screen, String ip, int partnerId, boolean isMxh, String isRealMoney) throws SQLException {

        UserEntity res = null;
        //String query = "{ call uspLogin(?, ?, ?, ?, ?,?, ?, ?) }";

        //Connection con = DBPoolConnection.getConnection();
        //Connection con = DBPoolConnectionGame.getConnection();
        Connection con = null;

        StringBuffer query = new StringBuffer();
        query.append("SELECT * FROM users WHERE username=? AND pass=?");

        // mLog.debug(query.toString() + " -- " + userName + " pas:" + password);
        ResultSet rs = null;
        //CallableStatement cs = null;
        PreparedStatement stm = null;
        try {
            con = DBGame.instance().getConnection();
            stm = con.prepareStatement(query.toString());

            stm.setString(1, userName);
            stm.setString(2, password);

            rs = stm.executeQuery();
            // mLog.debug(rs.toString());
            if (rs.first()) {
                res = new UserEntity();
//                res.mPassword = rs.getString("Password");
                res.mUid = rs.getLong("uid");

                res.mUsername = rs.getString("username");
                res.viewName = rs.getString("viewname");
                res.mPassword = password;

                res.lastLogin = rs.getTimestamp("lastDateLogin");
                if (rs.getString("sex").equals("Nam")) {
                    res.mIsMale = true;
                } else {
                    res.mIsMale = false;
                }
                res.avatarID = rs.getString("avatar");
                res.level = rs.getInt("level");
                //res.cash = rs.getLong("realmoney");
                //res.money = rs.getLong(isRealMoney);
                res.money = rs.getLong("money");
                res.realmoney = rs.getLong("realmoney");

                res.playsNumber = rs.getInt("total");

                //res.cellPhone = rs.getString("phoneNumber");
                res.cellPhone = "xxxx";
                res.isActive = rs.getBoolean("active");
                res.experience = rs.getInt("exp");

                //res.isOnline = rs.getBoolean("isOnline");
                res.isOnline = false;
                res.vipId = rs.getInt("level");

                //res.partnerId = rs.getInt("partnerId");
                res.partnerId = 1;
                res.stt = rs.getString("status");
                //res.isLock = rs.getInt("isLock");
                if (rs.getInt("utype") == 6) {
                    res.isLock = 1;
                }

                //res.vipName = "Vip " + Integer.toString(res.vipId);
                res.vipName = "Vip ";
                res.groupId = rs.getInt("groupId");
                res.utype = rs.getInt("utype");
                res.mail = rs.getString("email");
                // System.out.println("======================zp2:" + res.mIsMale);
            }

        } catch (Exception e) {
            mLog.debug("*********error login ******:", e);
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

            } catch (Exception e) {
                mLog.debug("Login Exception:", e);
            }

        }

        return res;
    }

    public void logout(long userId, String collectInfo) throws SQLException {

//        String query = "{ call uspLogout(?,?) }";
//        Connection con = DBPoolConnection.getConnection();
//        try {
//            CallableStatement cs = con.prepareCall(query);
//            cs.clearParameters();
//            cs.setLong(USER_ID_PARAM, userId);
//            cs.setString(COLLECT_INFO_PARAM, collectInfo);
//            cs.execute();
//            cs.clearParameters();
//            cs.close();
//        } finally {
//            con.close();
//        }
    }

    public void insertDeadSession(long userId, int phongId) throws SQLException {
        String query = "{ call uspInsertDeadSession(?,?) }";
        Connection con = DBPoolConnection.getConnection();
        try {
            CallableStatement cs = con.prepareCall(query);
            cs.clearParameters();
            cs.setLong(USER_ID_PARAM, userId);
            cs.setInt(PHONG_ID_PARAM, phongId);
            cs.execute();
            cs.clearParameters();
            cs.close();
        } finally {
            con.close();
        }

    }

    public UserEntity getUserInfo(String username, String isRealMoney) throws SQLException {
        UserEntity res = new UserEntity();

        StringBuffer query = new StringBuffer();
        //query.append("select userId,password,name,viewname,lastLogin,avatarId,level,cash,money,wonPlaysNumber,experience,address,avatarFileId,");
        //query.append("biaFileId,vipId,isActive,partnerId,phoneNumber,refCode,status,cmt FROM workinguser where name=? ");
        query.append("select * FROM users where username=? ");
        Connection con = null;
        PreparedStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBGame.instance().getConnection();
            //CallableStatement cs = con.prepareCall(query);
            cs = con.prepareStatement(query.toString());
            cs.setString(1, username);
            rs = cs.executeQuery();

            if (rs != null && rs.next()) {
                res.mPassword = rs.getString("pass");
                res.mUid = rs.getLong("uid");
                res.mUsername = rs.getString("username");
                res.viewName = rs.getString("viewname");
                res.lastLogin = rs.getDate("lastDateLogin");
                res.avatarID = rs.getString("avatar");
                res.level = rs.getInt("level");
                //res.cash = rs.getLong("realmoney");
                res.money = rs.getLong(isRealMoney);
                res.playsNumber = rs.getInt("total");
                res.experience = rs.getInt("exp");

                res.avFileId = 1;//rs.getLong("avatarFileId");
                res.biaFileId = 1;//rs.getLong("biaFileId");
                //res.vipId = rs.getInt("vipId");
                res.vipId = rs.getInt("level");
                res.isActive = rs.getBoolean("active");
                res.partnerId = 1;
                //res.cellPhone = rs.getString("phoneNumber") == null ? "" : rs.getString("phoneNumber").trim();
                res.cellPhone = "xxxx";
                res.vipName = "Vip " + Integer.toString(res.vipId);
                //res.refCode = rs.getInt("refCode");
                res.stt = rs.getString("status");
                res.groupId = rs.getInt("groupId");

//                res.address = rs.getString("address") == null ? "" : rs.getString("address").trim();
//                res.cmt = rs.getString("cmt") == null ? "" : rs.getString("cmt").trim();
                //res.name = rs.getString("viewname") == null ? "" : rs.getString("viewname").trim();
            }

            cs.clearParameters();
            cs.close();
        } finally {
            if (con != null) {
                con.close();// close init connection
            }
            if (cs != null) {
                cs.close();
            }
            if (rs != null) {
                rs.close();
            }

        }
        return res;
    }

    public UserEntity getUserInfoByViewName(String viewname, String isRealMoney) throws SQLException {
        UserEntity res = new UserEntity();

        StringBuffer query = new StringBuffer();
        //query.append("select userId,password,name,viewname,lastLogin,avatarId,level,cash,money,wonPlaysNumber,experience,address,avatarFileId,");
        //query.append("biaFileId,vipId,isActive,partnerId,phoneNumber,refCode,status,cmt FROM workinguser where name=? ");
        query.append("select * FROM users where viewname=? ");
        Connection con = null;
        PreparedStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBGame.instance().getConnection();
            //CallableStatement cs = con.prepareCall(query);
            cs = con.prepareStatement(query.toString());
            cs.setString(1, viewname);
            rs = cs.executeQuery();

            if (rs != null && rs.next()) {
                res.mPassword = rs.getString("pass");
                res.mUid = rs.getLong("uid");
                res.mUsername = rs.getString("username");
                res.viewName = rs.getString("viewname");
                res.lastLogin = rs.getDate("lastDateLogin");
                res.avatarID = rs.getString("avatar");
                res.level = rs.getInt("level");
                //res.cash = rs.getLong("realmoney");
                res.money = rs.getLong(isRealMoney);
                res.playsNumber = rs.getInt("total");
                res.experience = rs.getInt("exp");

                res.avFileId = 1;//rs.getLong("avatarFileId");
                res.biaFileId = 1;//rs.getLong("biaFileId");
                //res.vipId = rs.getInt("vipId");
                res.vipId = rs.getInt("level");
                res.isActive = rs.getBoolean("active");
                res.partnerId = 1;
                //res.cellPhone = rs.getString("phoneNumber") == null ? "" : rs.getString("phoneNumber").trim();
                res.cellPhone = "xxxx";
                res.vipName = "Vip " + Integer.toString(res.vipId);
                res.groupId = rs.getInt("groupId");
                //res.refCode = rs.getInt("refCode");
                res.stt = rs.getString("status");

            }

        } finally {
            if (con != null) {
                con.close();// close init connection
            }
            if (cs != null) {
                cs.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return res;
    }

    public UserEntity getUserInfo(long uid, String isRealMoney) throws SQLException {
        UserEntity res = null;

        //String query = "{ call uspGetUserInfoByUid(?) }";
        StringBuffer query = new StringBuffer();
//        query.append("select userId,password,name,viewname,lastLogin,avatarId,level,cash,money,wonPlaysNumber,experience,address,avatarFileId,");
//        query.append("biaFileId,vipId,isActive,partnerId,phoneNumber,refCode,status,cmt FROM workinguser where userId=? ");
        query.append("select * from users where uid = ?");
        Connection con = null;
        PreparedStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBGame.instance().getConnection();
            //CallableStatement cs = con.prepareCall(query);
            cs = con.prepareStatement(query.toString());
            cs.setLong(1, uid);
            rs = cs.executeQuery();

            if (rs != null && rs.next()) {
                res = new UserEntity();
                res.mPassword = rs.getString("pass");
                res.mUid = rs.getLong("uid");
                res.mUsername = rs.getString("username");
                res.viewName = rs.getString("viewname");

                //System.out.println("rs.getString(\"avatar\"):"+rs.getString("avatar"));
                res.avatarID = rs.getString("avatar");
                res.level = rs.getInt("level");
                res.realmoney = rs.getLong("realmoney");
                res.money = rs.getLong(isRealMoney);
                res.playsNumber = rs.getInt("total");
                res.experience = rs.getInt("exp");

                res.avFileId = 1;//rs.getLong("avatarFileId");
                res.biaFileId = 1;//rs.getLong("biaFileId");
                //res.vipId = rs.getInt("vipId");
                res.vipId = rs.getInt("level");
                res.isActive = rs.getBoolean("active");
                res.partnerId = 1;
                //res.cellPhone = rs.getString("phoneNumber") == null ? "" : rs.getString("phoneNumber").trim();
                res.cellPhone = "xxxx";
                res.vipName = "Vip " + Integer.toString(res.vipId);
                //res.refCode = rs.getInt("refCode");
                res.stt = rs.getString("status");
                res.utype = rs.getInt("utype");
                res.lastLogin = rs.getDate("lastDateLogin");
                res.mail = rs.getString("email");
                res.groupId = rs.getInt("groupId");

                if (rs.getString("sex").equals("Nam")) {
                    res.mIsMale = true;
                } else {
                    res.mIsMale = false;
                }
//                res.address = rs.getString("address") == null ? "" : rs.getString("address").trim();
//                res.cmt = rs.getString("cmt") == null ? "" : rs.getString("cmt").trim();
                //res.name = rs.getString("viewname") == null ? "" : rs.getString("viewname").trim();

            }

        } finally {
            if (con != null) {
                con.close();// close init connection
            }
            if (rs != null) {
                rs.close();// close init connection
            }
            if (cs != null) {
                cs.close();// close init connection
            }

        }
        return res;
    }

    public UserEntity getUserInfoFullMoney(long uid) throws SQLException {
        UserEntity res = null;

        //String query = "{ call uspGetUserInfoByUid(?) }";
        StringBuffer query = new StringBuffer();
//        query.append("select userId,password,name,viewname,lastLogin,avatarId,level,cash,money,wonPlaysNumber,experience,address,avatarFileId,");
//        query.append("biaFileId,vipId,isActive,partnerId,phoneNumber,refCode,status,cmt FROM workinguser where userId=? ");
        query.append("select * from users where uid = ?");
        Connection con = null;
        PreparedStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBGame.instance().getConnection();
            //CallableStatement cs = con.prepareCall(query);
            cs = con.prepareStatement(query.toString());
            cs.setLong(1, uid);
            rs = cs.executeQuery();

            if (rs != null && rs.next()) {
                res = new UserEntity();
                res.mPassword = rs.getString("pass");
                res.mUid = rs.getLong("uid");
                res.mUsername = rs.getString("username");
                res.viewName = rs.getString("viewname");

                //System.out.println("rs.getString(\"avatar\"):"+rs.getString("avatar"));
                res.avatarID = rs.getString("avatar");
                res.level = rs.getInt("level");
                //res.cash = rs.getLong("realmoney");
                res.money = rs.getLong("money");
                res.realmoney = rs.getLong("realmoney");
                res.playsNumber = rs.getInt("total");
                res.experience = rs.getInt("exp");

                res.avFileId = 1;//rs.getLong("avatarFileId");
                res.biaFileId = 1;//rs.getLong("biaFileId");
                //res.vipId = rs.getInt("vipId");
                res.vipId = rs.getInt("level");
                res.isActive = rs.getBoolean("active");
                res.partnerId = 1;
                //res.cellPhone = rs.getString("phoneNumber") == null ? "" : rs.getString("phoneNumber").trim();
                res.cellPhone = "xxxx";
                res.vipName = "Vip " + Integer.toString(res.vipId);
                //res.refCode = rs.getInt("refCode");
                res.stt = rs.getString("status");
                res.utype = rs.getInt("utype");
                res.lastLogin = rs.getDate("lastDateLogin");
                res.mail = rs.getString("email");
                res.groupId = rs.getInt("groupId");

                if (rs.getString("sex").equals("Nam")) {
                    res.mIsMale = true;
                } else {
                    res.mIsMale = false;
                }
//                res.address = rs.getString("address") == null ? "" : rs.getString("address").trim();
//                res.cmt = rs.getString("cmt") == null ? "" : rs.getString("cmt").trim();
                //res.name = rs.getString("viewname") == null ? "" : rs.getString("viewname").trim();

            }

        } finally {
            if (con != null) {
                con.close();// close init connection
            }
            if (rs != null) {
                rs.close();// close init connection
            }
            if (cs != null) {
                cs.close();// close init connection
            }

        }
        return res;
    }

    public UserEntity getUserInfo(long uid) throws SQLException {
        UserEntity res = null;

        //String query = "{ call uspGetUserInfoByUid(?) }";
        StringBuffer query = new StringBuffer();
//        query.append("select userId,password,name,viewname,lastLogin,avatarId,level,cash,money,wonPlaysNumber,experience,address,avatarFileId,");
//        query.append("biaFileId,vipId,isActive,partnerId,phoneNumber,refCode,status,cmt FROM workinguser where userId=? ");
        query.append("select * from users where uid = ?");
        Connection con = null;
        PreparedStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBGame.instance().getConnection();
            //CallableStatement cs = con.prepareCall(query);
            cs = con.prepareStatement(query.toString());
            cs.setLong(1, uid);
            rs = cs.executeQuery();

            if (rs != null && rs.next()) {
                res = new UserEntity();
                res.mPassword = rs.getString("pass");
                res.mUid = rs.getLong("uid");
                res.mUsername = rs.getString("username");
                res.viewName = rs.getString("viewname");

                //System.out.println("rs.getString(\"avatar\"):"+rs.getString("avatar"));
                res.avatarID = rs.getString("avatar");
                res.level = rs.getInt("level");
                res.realmoney = rs.getLong("realmoney");
                res.money = rs.getLong("money");
                res.playsNumber = rs.getInt("total");
                res.experience = rs.getInt("exp");

                res.avFileId = 1;//rs.getLong("avatarFileId");
                res.biaFileId = 1;//rs.getLong("biaFileId");
                //res.vipId = rs.getInt("vipId");
                res.vipId = rs.getInt("level");
                res.isActive = rs.getBoolean("active");
                res.partnerId = 1;
                //res.cellPhone = rs.getString("phoneNumber") == null ? "" : rs.getString("phoneNumber").trim();
                res.cellPhone = "xxxx";
                res.vipName = "Vip " + Integer.toString(res.vipId);
                //res.refCode = rs.getInt("refCode");
                res.stt = rs.getString("status");
                res.utype = rs.getInt("utype");
                res.lastLogin = rs.getDate("lastDateLogin");
                res.mail = rs.getString("email");
                res.groupId = rs.getInt("groupId");
                if (rs.getString("sex").equals("Nam")) {
                    res.mIsMale = true;
                } else {
                    res.mIsMale = false;
                }
//                res.address = rs.getString("address") == null ? "" : rs.getString("address").trim();
//                res.cmt = rs.getString("cmt") == null ? "" : rs.getString("cmt").trim();
                //res.name = rs.getString("viewname") == null ? "" : rs.getString("viewname").trim();

            }

        } finally {
            if (con != null) {
                con.close();// close init connection
            }
            if (rs != null) {
                rs.close();// close init connection
            }
            if (cs != null) {
                cs.close();// close init connection
            }

        }
        return res;
    }

    public UserEntity getUserByDeviceID(String deviceID, String isRealMoney) throws SQLException {
        UserEntity res = new UserEntity();

        StringBuffer query = new StringBuffer();

        query.append("select * from users where deviceID = ? and groupId = ?");
        Connection con = null;
        PreparedStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBGame.instance().getConnection();
            cs = con.prepareStatement(query.toString());
            cs.setString(1, deviceID);
            cs.setInt(2, 2);
            rs = cs.executeQuery();

            if (rs != null && rs.next()) {
                res.mPassword = rs.getString("pass");
                res.mUid = rs.getLong("uid");
                res.mUsername = rs.getString("username");
                res.viewName = rs.getString("viewname");
                res.lastLogin = rs.getDate("lastDateLogin");
                res.avatarID = rs.getString("avatar");
                res.level = rs.getInt("level");
                //res.cash = rs.getLong("realmoney");
//                res.money = rs.getLong(isRealMoney);
                res.money = rs.getLong("money");
                res.realmoney = rs.getLong("realmoney");
                res.playsNumber = rs.getInt("total");
                res.experience = rs.getInt("exp");

                res.avFileId = 1;//rs.getLong("avatarFileId");
                res.biaFileId = 1;//rs.getLong("biaFileId");

                res.vipId = rs.getInt("level");
                res.isActive = rs.getBoolean("active");
                res.partnerId = 1;

                res.cellPhone = "xxxx";
                res.vipName = "Vip " + Integer.toString(res.vipId);
                res.groupId = rs.getInt("groupId");
                //res.stt = rs.getString("status");

            }

        } finally {
            if (con != null) {
                con.close();// close init connection
            }
            if (rs != null) {
                rs.close();
            }
            if (cs != null) {
                cs.close();
            }
        }
        return res;
    }

    public long getMoney(long uid, String isRealMoney) {
        long money = -1;
        StringBuffer query = new StringBuffer();

        query.append("select " + isRealMoney + " FROM users where uid=? ");

        Connection con = null;

        PreparedStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBGame.instance().getConnection();
            cs = con.prepareStatement(query.toString());
            cs.setLong(1, uid);
            rs = cs.executeQuery();
            if (rs != null && rs.next()) {

                money = rs.getLong(isRealMoney);

            }

        } catch (SQLException e) {

        } finally {

            if (con != null) {
                try {
                    con.close();// close init connection
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();// close init connection
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (cs != null) {
                try {
                    cs.close();// close init connection
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return money;
    }

    public long getTotalMoneyBot(int utype, String isRealMoney) throws SQLException {
        long money = 0;
        StringBuffer query = new StringBuffer();

        query.append("select sum(" + isRealMoney + ") as total FROM users where utype=? ");

        Connection con = null;

        PreparedStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBGame.instance().getConnection();
            cs = con.prepareStatement(query.toString());
            cs.setLong(1, utype);
            rs = cs.executeQuery();
            if (rs != null && rs.next()) {

                money = rs.getLong("total");

            }

        } finally {

            if (con != null) {
                con.close();// close init connection
            }
            if (rs != null) {
                rs.close();// close init connection
            }
            if (cs != null) {
                cs.close();// close init connection
            }
        }
        return money;
    }

    public boolean userIsExist(String username) throws DBException, SQLException {
        boolean result = false;
        String query = "SELECT username FROM users WHERE username=?;";
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = DBGame.instance().getConnection();
            st = con.prepareStatement(query);
            st.setString(1, username);
            rs = st.executeQuery();
            if (rs != null && rs.next()) {
                result = true;
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

        return result;
    }

    public boolean userViewNameIsExist(String viewname) throws DBException, SQLException {
        boolean result = false;
        String query = "SELECT viewname FROM users WHERE viewname=?;";
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = DBGame.instance().getConnection();
            st = con.prepareStatement(query);
            st.setString(1, viewname);
            rs = st.executeQuery();
            if (rs != null && rs.next()) {
                result = true;
            }

        } finally {

            if (con != null) {
                con.close();
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

        return result;
    }

    public long updateUserMoney(long money, boolean isWin, long uid, String desc, int experience, int logTypeId, long matchId, int matchNum, String isRealMoney, long cuoc, long ownerid) {
        //boolean realMoney = true;
        int zoneId = logTypeId;
        long cash = 0;
        long cashBefore = this.getMoney(uid, isRealMoney);
        StringBuilder query = new StringBuilder();
        int currExp = experience;
        int levelBefore = SimplePlayer.getLevel(currExp, zoneId);
        int expUp = this.getExpUpByZoneId(logTypeId, isWin);
        if (isWin) {
            cashBefore += money;
            query.append("update users set ").append(isRealMoney).append(" = ?, ");
            query.append("win = win + ?,total = total + ? ");
            if (currExp != -1) {//khong su dung cho taixiu,xocdia
                query.append(", level = ? ");
                query.append(", exp = exp + " + expUp + " ");
                experience += expUp;
            }

        } else {
            if (cashBefore > money) {
                cashBefore -= money;
            } else {
                cashBefore = 0;
            }

            //mLog.debug("cashBefore:"+cashBefore);
            query.append("update users set ").append(isRealMoney).append(" = ?, ");
            query.append("win = win + ?,total = total + ? ");
            if (currExp != -1) {//khong su dung cho taixiu,xocdia
                query.append(", level = ? ");
                query.append(", exp = exp + " + expUp + " ");
                experience += expUp;
            }

        }

        query.append("WHERE uid = ?");

        //experience = SimplePlayer.countExperience(isWin, money, zoneId);
        int level = SimplePlayer.getLevel(experience, zoneId);
        if (level > levelBefore) {
            int levelUpMoney = SimplePlayer.getLevelUpMoney(level);
            cashBefore += levelUpMoney;
            desc += ", lên level " + level + " được cộng " + levelUpMoney;
        }
        //mLog.debug("query:"+query.toString());
        Connection con = null;
        Connection conGame = null;

        PreparedStatement stm = null;
        try {
            conGame = DBGame.instance().getConnection();
            //mLog.debug("query:"+query.toString() +  " uid:"+uid + " currExp:"+currExp);
            stm = conGame.prepareStatement(query.toString());
            stm.setLong(1, cashBefore);
            stm.setInt(2, isWin ? 1 : 0);
            stm.setInt(3, 1);
            if (currExp != -1) {
                stm.setInt(4, level);
                stm.setLong(5, uid);
            } else {

                stm.setLong(4, uid);
            }

            stm.executeUpdate();

        } catch (Exception e) {
            mLog.debug("insertLogVasc error :", e);
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
                if (con != null) {
                    con.close();
                }
                if (conGame != null) {
                    conGame.close();
                }

            } catch (SQLException e) {
                mLog.debug("error close" + e.getMessage());
            }
        }

        //insert logvasc
        LogvascDB logvasc = new LogvascDB();

        try {
            logvasc.insertLogVasc(uid, (double) (isWin ? money : -money), desc, logTypeId, isWin, experience, cashBefore, matchId, matchNum, isRealMoney, cuoc, ownerid);
            if (zoneId == ZoneID.BIDA || zoneId == ZoneID.BIDA_FOUR || zoneId == ZoneID.BIDA_PHOM || zoneId == ZoneID.COTUONG 
                    || zoneId == ZoneID.SOCCERSTART || zoneId == ZoneID.HEADBALL || zoneId == ZoneID.SHOOTS || zoneId == ZoneID.FOOTBALL || zoneId == ZoneID.DRAGGER) {
                zoneId = logTypeId;
            } else {
                zoneId = logTypeId - SimpleTable.LOG_TYPE_GAME_START;

            }
            if (zoneId == ZoneID.BIDA || zoneId == ZoneID.BIDA_FOUR || zoneId == ZoneID.BIDA_PHOM || zoneId == ZoneID.COTUONG || zoneId == ZoneID.NEW_BA_CAY || zoneId == ZoneID.PHOM
                    || zoneId == ZoneID.BINH || zoneId == ZoneID.POKER || zoneId == ZoneID.TIENLEN || zoneId == ZoneID.SOCCERSTART || zoneId == ZoneID.HEADBALL || zoneId == ZoneID.SHOOTS 
                    || zoneId == ZoneID.FOOTBALL || zoneId == ZoneID.DRAGGER) {//log thang thua
                LogGameDataDB logev = new LogGameDataDB();
                int typeMoney = 0;
                if (isRealMoney.equals(AIOConstants.PRIFIX_REALMONEY)) {
                    typeMoney = 1;
                }
                logev.insertLogGameData(uid, zoneId, isWin, experience, typeMoney);
            }

        } catch (Exception ex) {
            mLog.debug("insertLogVasc:", ex);
        }
        //update cash in cache. It shoudnt do this case in
        try {
            CacheUserInfo.updateUserCashFromDB(uid, cashBefore);
        } catch (Exception e) {
            mLog.debug("updateUserMoney error:", e);
        }
        //mLog.debug("return cash:"+uid +":" + cash);
        return cashBefore;
    }

    public long updateUserMoneyNotLog(long money, boolean isWin, long uid, String desc, int experience, int logTypeId, long matchId, int matchNum, String isRealMoney)
            throws SQLException {
        //boolean realMoney = true;

        long cash = 0;

        StringBuffer query = new StringBuffer();

        if (isWin) {

            query.append("update users set " + isRealMoney + " = " + isRealMoney + " + ?, ");
            query.append("win = win + ?,total = total + ? ");

        } else {

            query.append("update users set " + isRealMoney + " = " + isRealMoney + " - ?, ");
            query.append("win = win + ?,total = total + ? ");

        }

        query.append("WHERE uid = ?");

        // int zoneId = logTypeId - SimpleTable.LOG_TYPE_GAME_START;
        //experience = SimplePlayer.countExperience(isWin, money, zoneId);
//        int level = SimplePlayer.getLevel(experience,zoneId);
//        mLog.debug("level:"+level);
        Connection con = null;
        Connection conGame = null;

        PreparedStatement stm = null;
        try {
            conGame = DBGame.instance().getConnection();

            stm = conGame.prepareStatement(query.toString());
            stm.setLong(1, money);
            stm.setInt(2, isWin ? 1 : 0);
            stm.setInt(3, 1);
            stm.setLong(4, uid);
            stm.executeUpdate();

        } catch (Exception e) {
            mLog.debug("insertLogVasc error :", e);
        } finally {
            if (con != null) {
                con.close();
            }
            if (conGame != null) {
                conGame.close();
            }
            if (stm != null) {
                stm.close();
            }
        }

        //update cash in cache. It shoudnt do this case in
        long cashBefore = this.getMoney(uid, isRealMoney);
//        try {
//            CacheUserInfo.updateUserCashFromDB(uid, cashBefore);
//        } catch (Exception e) {
//            mLog.debug("updateUserMoney error:", e);
//        }
        //mLog.debug("return cash:"+uid +":" + cash);

        return cashBefore;
    }

    public long updateUserData(UserEntity user, long moneyChange, String desc, String isRealMoney)
            throws DBException, SQLException {
        //boolean realMoney = true;

        //long cashBefore = this.getMoney(user.mUid, realMoney);
        long cash = 0;

        StringBuffer query = new StringBuffer();

        query.append("update users set " + isRealMoney + " =  ?, lastDateLogin = ? ");

        query.append("WHERE uid = ?");

        int zoneId = 0;

        int experience = 1;

        Connection con = null;
        Connection conGame = null;

        PreparedStatement stm = null;
        try {
            conGame = DBGame.instance().getConnection();
            //CallableStatement cs = con.prepareCall(query);

            stm = conGame.prepareStatement(query.toString());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String now = dateFormat.format(date);

            stm.setLong(1, user.money);
            stm.setString(2, now);
            stm.setLong(3, user.mUid);
            stm.executeUpdate();

            //insert logvasc
            if (!desc.equals("")) {
                LogvascDB logvasc = new LogvascDB();

                try {
                    logvasc.insertLogVasc(user.mUid, (double) moneyChange, desc, 0, false, 0, user.money, 0, 0, isRealMoney, 0, 0);
                    if (zoneId == ZoneID.BIDA || zoneId == ZoneID.BIDA_FOUR || zoneId == ZoneID.BIDA_PHOM) {//log thang thua
                        LogGameDataDB logev = new LogGameDataDB();
                        int typeMoney = 0;
                        if (isRealMoney.equals(AIOConstants.PRIFIX_REALMONEY)) {
                            typeMoney = 1;
                        }
                        logev.insertLogGameData(user.mUid, zoneId, false, experience, typeMoney);
                    }

                } catch (Exception ex) {
                    mLog.debug("insertLogVasc:", ex);
                }
            }
        } catch (Exception e) {
            mLog.debug("insertLogVasc error :", e);
        } finally {
            if (con != null) {
                con.close();
            }
            if (conGame != null) {
                conGame.close();
            }
            if (stm != null) {
                stm.close();
            }
        }

        //update cash in cache. It shoudnt do this case in
        try {
            CacheUserInfo.updateUserCashFromDB(user.mUid, cash);
        } catch (Exception e) {
            mLog.debug("updateUserMoney error:", e);
        }
        mLog.debug("return cash:" + user.mUid + ":" + cash);
        return cash;
    }

    public void updateUserLastLogin(UserEntity user)
            throws DBException, SQLException {
        StringBuffer query = new StringBuffer();

        query.append("update users set lastDateLogin = ? ");

        query.append("WHERE uid = ?");

        Connection conGame = null;

        PreparedStatement stm = null;
        try {
            conGame = DBGame.instance().getConnection();

            stm = conGame.prepareStatement(query.toString());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String now = dateFormat.format(date);

            stm.setString(1, now);
            stm.setLong(2, user.mUid);
            stm.executeUpdate();

        } catch (Exception e) {
            mLog.debug("update lastlogin error :", e);
        } finally {

            if (conGame != null) {
                conGame.close();
            }
            if (stm != null) {
                stm.close();
            }
        }

    }

    public long updateUserLastLoginGiftDay(UserEntity user)
            throws DBException, SQLException {
        mLog.debug("ok vao cong tien hang ngay:" + user.viewName);
        StringBuffer query = new StringBuffer();

        query.append("update users set lastDateLogin = ?, realmoney = realmoney + ? ");

        query.append("WHERE uid = ?");

        Connection conGame = null;
        long realmoney = user.money;
        PreparedStatement stm = null;
        try {
            conGame = DBGame.instance().getConnection();

            stm = conGame.prepareStatement(query.toString());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String now = dateFormat.format(date);

            stm.setString(1, now);
            stm.setLong(2, AIOConstants.GIFT_LOGIN_BY_DAY);
            stm.setLong(3, user.mUid);
            stm.executeUpdate();
            realmoney += AIOConstants.GIFT_LOGIN_BY_DAY;
//start insert mail

            try {
                MailDB mailDB = new MailDB();
                String desc = "Đăng nhập hàng ngày Tặng " + AIOConstants.GIFT_LOGIN_BY_DAY + " " + AIOConstants.STR_MONEY_SYSTEM_GOLD + " khi tài khoản còn dưới 1k";
                mailDB.insert(user.mUid, "Hệ thống", "Đăng nhập hàng ngày", desc);
                //insert mail users_mail
                mailDB.updateCoundMailAdd(user.mUid);
                //insert logvasc
                LogvascDB logvasc = new LogvascDB();
                try {
                    logvasc.insertLogVasc(user.mUid, (double) AIOConstants.GIFT_LOGIN_BY_DAY, desc, 000, false, 0, (user.money + AIOConstants.GIFT_LOGIN_BY_DAY), 0, 0, AIOConstants.PRIFIX_REALMONEY, 0, 0);
                } catch (Exception ex) {
                    mLog.debug("insertLogVasc:", ex);
                }

            } catch (Exception e) {

            }

            //end start insert mail
        } catch (Exception e) {
            mLog.debug("update lastlogin error :", e);
        } finally {

            if (conGame != null) {
                conGame.close();
            }
            if (stm != null) {
                stm.close();
            }
        }
        return realmoney;
    }

    public int updateUserMoneyForBonus(long uid, long money, int eventId, int eventType, String deviceUid, String socialId) throws DBException, SQLException {
        int result = 0;
        String query = "{?= call uspUpdateUserMoneyForBonus(?,?,?,?,?,?) }";
//    	  String query = "{ call uspUpdateUserMoneyForBonus("+uid+","+money+","+eventId+","+eventType+",'"+deviceUid+"','"+socialId+"') }";
        Connection con = DBPoolConnection.getConnection();

        try {
            CallableStatement cs = con.prepareCall(query);
            cs.registerOutParameter(1, Types.INTEGER);

            cs.setLong("uid", uid);
            cs.setLong("money", money);
            cs.setInt("eventId", eventId);
            cs.setInt("eventType", eventType);
            cs.setString("deviceUid", deviceUid);
            cs.setString("socialId", socialId);

            cs.execute();
            result = cs.getInt(1);
            cs.close();

        } finally {
            con.close();
        }

        return result;
    }

    public long updateUserMoneyForTP(long money, boolean isWin, long uid, String desc, int experience, int logTypeId, int achievementQuestion)
            throws DBException, SQLException {

        long cash = 0;
        String query = "{ call uspUpdateUserMoneyForTP(?,?,?,?, ?, ?,?) }";
        //SimpleConnnection conn = MasterPoolConnection.checkOut();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBGame.instance().getConnection();
            cs = con.prepareCall(query);
            cs.setLong(1, uid);
            cs.setInt(2, (int) (isWin ? money : -money));
            cs.setString(3, desc);
            cs.setInt(4, logTypeId);//Default log type id = 4;
            cs.setInt(6, experience);//Default log type id = 4;
            cs.setBoolean(5, isWin);
            cs.setInt(7, achievementQuestion);//

            rs = cs.executeQuery();
            if (rs != null && rs.next()) {
                cash = rs.getLong("cash");
            }

            //conn.commit();
            cs.clearParameters();
            rs.close();
            cs.close();
        } finally {
            if (con != null) {
                con.close(); // close connection which is opened from this function
            }
            if (cs != null) {
                cs.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

        //update cash in cache. It shoudnt do this case in 
        CacheUserInfo.updateUserCashFromDB(uid, cash);

        return cash;
    }

    public void notMinus(String isRealMoney) throws SQLException {
        //String query = "{call uspNotMinus()}";
        String query = "update users set " + isRealMoney + " = 0 where " + isRealMoney + " < 0";
        Connection con = null;
        PreparedStatement stm = null;

        try {
            //CallableStatement cs = con.prepareCall(query);
            con = DBGame.instance().getConnection();
            stm = con.prepareStatement(query);
//            cs.execute();
//            cs.close();
            stm.executeUpdate();
        } finally {
            if (con != null) {
                con.close();
            }
            if (stm != null) {
                stm.close();
            }

        }
    }

    public void updateMatchTips(long matchId, int matchNum) throws SQLException {

        return;

//        String query = "{call uspUpdateMatchTips(?,?)}";
//        Connection con = DBPoolConnection.getConnection();
//        
//        try {
//            CallableStatement cs = con.prepareCall(query);
//            cs.setLong(1, matchId);
//            cs.setInt(2, matchNum);
//            
//            cs.execute();
//            cs.close();
//            
//        } finally {
//            con.close();
//        }
    }

    public List getLockUser() throws SQLException {

        ArrayList userList = new ArrayList();

        String query = "select * from UserLock";
        Connection conn = null;
        //conn = DBPoolConnection.getConnection();
        conn = DBVip.instance().getConnection();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {

            stm = conn.prepareStatement(query);

            rs = stm.executeQuery();

            if (rs.first()) {

                while (rs.next()) {
                    long userId = rs.getLong("userId");
                    userList.add(userId);
                }

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
        return userList;
    }

    public void resetLockUser() throws SQLException {
        //String query = "{call resetLockUser()}";
        String query = "delete from UserLock";

        Connection con = null;

        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBVip.instance().getConnection();
            //   CallableStatement cs = con.prepareCall(query);
//            cs.execute();
//            cs.close();
            stm = con.prepareStatement(query);

            rs = stm.executeQuery();
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
    }

    public long transferMoney(long s_uid, String d_name, long money) throws SQLException, DBException {
        String query = "{?= call Transfer(?,?,?) }";

        Connection conn = DBPoolConnection.getConnection();
        long d_uid = -1;
        try {
            CallableStatement cs = conn.prepareCall(query);
            cs.registerOutParameter(1, Types.BIGINT);

            cs.setLong(3, s_uid);
            cs.setString(4, d_name);
            cs.setLong(2, money);
            cs.execute();

            //conn.commit();
            d_uid = cs.getLong(1);

            cs.close();
        } finally {
            conn.close();
        }

        return d_uid;

    }

//    public int changePassword(long userId, String oldPassword, String newPassword) throws DBException, SQLException {
//
//
//
//        String query = "{?= call uspUpdateUserInfo(?,?,?,?, ?, ?, ?) }";
//        Connection con = DBPoolConnection.getConnection();
//        int ret = -1;
//        try {
//            CallableStatement cs = con.prepareCall(query);
//            cs.registerOutParameter(1, Types.INTEGER);
//
//            cs.setLong(2, userId);
//
//
//            cs.setString(3, null);
//
//
//
//            cs.setString(4, null);
//
//
//
//            cs.setString(5, oldPassword);
//
//            cs.setString(6, newPassword);
//
//            cs.setString(7, null);
//
//            cs.setString(8, null);
//
//            cs.execute();
//            ret = cs.getInt(1);
//
//            cs.close();
//        } finally {
//            con.close();
//        }
//        return ret;
//    }
    public int updateUserInfo(long userId, String name, String cmt, String address) throws DBException, SQLException {

        //String query = "{?= call uspUpdateUserInfo(?,?,?,?) }";
        //stm = conn.prepareStatement("UPDATE user_params SET end_date=?, last_utype=?, type=? WHERE uid=?");
        String query = "UPDATE users SET viewname=? WHERE uid=?";
        Connection conn = null;

        int ret = -1;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = DBGame.instance().getConnection();
            //CallableStatement cs = con.prepareCall(query);
            stm = conn.prepareStatement(query);

            //  stm.registerOutParameter(1, Types.INTEGER);
//            cs.setLong(2, userId);
//            cs.setString(3, name);
//            cs.setString(4, cmt);
//            cs.setString(5, address);
            stm.setString(1, name);
            stm.setLong(2, userId);

            rs = stm.executeQuery();
            if (rs != null) {
                if (rs.next()) {
                    ret = rs.getInt(1);
                }
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
        return ret;
    }

    public void updateUserMxhInfo(long userId, int cityId, String address, int jobId, Date birthday, String hobby,
            String nickSkype, String nickYahoo, String phoneNumber, boolean sex, long avatarFileId, String status, int characterId) throws SQLException {

        String query = "{call uspUpdateMxhUserInfo(?,?,?,?, ?, ?, ?, ?, ?, ?,?,?) }";
        Connection con = DBPoolConnection.getConnection();

        try {
            CallableStatement cs = con.prepareCall(query);

            cs.setLong(USER_ID_PARAM, userId);
//            cs.setBoolean(SEX_PARAM, sex);

            if (cityId == 0) {
                cs.setString(CITY_ID_PARAM, null);
            } else {
                cs.setInt(CITY_ID_PARAM, cityId);
            }

            if (address == null || address.equals("")) {
                cs.setString(ADDRESS_PARAM, null);
            } else {
                cs.setString(ADDRESS_PARAM, address);
            }

            if (jobId == 0) {
                cs.setString(JOB_ID_PARAM, null);
            } else {
                cs.setInt(JOB_ID_PARAM, jobId);
            }

            if (birthday == null || birthday.equals("")) {
                cs.setString(BIRTHDAY_PARAM, null);
            } else {
                cs.setTimestamp(BIRTHDAY_PARAM, new Timestamp(birthday.getTime()));
            }

            if (hobby == null || hobby.equals("")) {
                cs.setString(HOBBY_PARAM, null);
            } else {
                cs.setString(HOBBY_PARAM, hobby);
            }

            if (nickSkype == null || nickSkype.equals("")) {
                cs.setString(NICK_SKYPE_PARAM, null);
            } else {
                cs.setString(NICK_SKYPE_PARAM, nickSkype);
            }

            if (nickYahoo == null || nickYahoo.equals("")) {
                cs.setString(NICK_YAHOO_PARAM, null);
            } else {
                cs.setString(NICK_YAHOO_PARAM, nickSkype);
            }

            if (phoneNumber == null || phoneNumber.equals("")) {
                cs.setString(PHONE_NUMBER_PARAM, null);
            } else {
                cs.setString(PHONE_NUMBER_PARAM, phoneNumber);
            }

            if (avatarFileId == 0) {
                cs.setString(AVATAR_FILE_ID_PARAM, null);
            } else {
                cs.setLong(AVATAR_FILE_ID_PARAM, avatarFileId);
            }

            if (status == null || status.equals("")) {
                cs.setString(STATUS_PARAM, null);
            } else {
                cs.setString(STATUS_PARAM, status);
            }

            cs.setInt(CHARACTER_ID_PARAM, characterId);

            cs.execute();

            cs.close();
        } finally {
            con.close();
        }

    }

    public static long registerUser(String username, String password, int sex,
            int age, String mail, String phone, String key, int partnerId) throws Exception {

        String query = "{?= call uspRegisterUserGC(?,?,?,?,?,?, ?, ?) }";
        Connection conn = DBPoolConnection.getConnection();

        CallableStatement cs = conn.prepareCall(query);
        try {

            cs.clearParameters();
            cs.registerOutParameter(1, java.sql.Types.BIGINT);
            cs.setString(USER_NAME_PARAM, username);
            cs.setString(PASSWORD_PARAM, password);
            cs.setInt(SEX_PARAM, sex);
            cs.setInt(AGE_PARAM, age);
            cs.setString(EMAIL_PARAM, mail);
            cs.setString(PHONE_PARAM, phone);
            cs.setString(KEY_ID_PARAM, key);
            cs.setInt(PARTNER_ID_PARAM, partnerId);
            cs.execute();

            long uid = cs.getLong(1);
            cs.close();
            return uid;
        } finally {
            conn.close();
        }
    }

    public long registerUserMxh(String username, String password,
            int sex,
            String phone,
            int partnerId,
            boolean chkPartnerSide,
            long introduceId, boolean isMxh,
            String refCode,
            int registerTime,
            String deviceUid, int groupId, int deviceType, String ip, String isRealMoney, String countryCode) throws Exception {

        //String query = "{?= call uspRegUser(?,?,?,?,?,?,?,?,?,?,?,?) }";
        String query = "INSERT INTO users (username, pass, created, lastDateLogin, " + isRealMoney + ", groupId, deviceID,money,deviceType,ip) VALUES (? , ? , ? , ?, ? ,?,?,?,?,?)";

        //Connection con = DBPoolConnectionGame.getConnection();
        Connection con = null;

        PreparedStatement stm = null;
        ResultSet rs = null;
        int moneyGiftRegister = 0;
        try {
            con = DBGame.instance().getConnection();
            stm = con.prepareStatement(query, 1);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String now = dateFormat.format(date);

            stm.setString(1, username);
            stm.setString(2, password);
            stm.setString(3, now);
            stm.setString(4, now);
            if (isRealMoney.equals("money")) {
                moneyGiftRegister = 0;
                stm.setInt(5, moneyGiftRegister);//km khi dang ky tk fb
            } else {
                if (isMxh) {
                    //stm.setInt(5, 2000);//km khi dang ky tk fb
                    //dung cho rubvip
                    moneyGiftRegister = AIOConstants.GIFT_REGISTER_FB;
                    stm.setInt(5, AIOConstants.GIFT_REGISTER_FB);//km khi dang ky tk fb
                } else {
                    moneyGiftRegister = AIOConstants.GIFT_REGISTER;
                    stm.setInt(5, moneyGiftRegister);
                }
            }

            stm.setInt(6, groupId);
            stm.setString(7, deviceUid);
            stm.setInt(8, 50000);//dung cho langbagian
            stm.setInt(9, deviceType);//dung cho langbagian
            stm.setString(10, ip);//dung cho langbagian
            stm.executeUpdate();

            rs = stm.getGeneratedKeys();
            long uid = 0;
            if (rs.next()) {
                uid = rs.getLong(1);
            }
            //start insert mail
            if (moneyGiftRegister > 0 && uid > 0) {
                try {
                    MailDB mailDB = new MailDB();
                    String desc = Languages.instance(countryCode).getString("nphtangban") + moneyGiftRegister + Languages.instance(countryCode).getString("quan") + Languages.instance(countryCode).getString("khoinghiep");
                    mailDB.insert(uid, Languages.instance(countryCode).getString("hethong"), Languages.instance(countryCode).getString("dangky"), desc);
                    //insert mail users_mail
                    mailDB.insertUserMail(uid, 1);
                    //insert logvasc
                    LogvascDB logvasc = new LogvascDB();
                    try {
                        logvasc.insertLogVasc(uid, (double) moneyGiftRegister, desc, 000, false, 0, moneyGiftRegister, 0, 0, isRealMoney, 0, 0);
                    } catch (Exception ex) {
                        mLog.debug("insertLogVasc:", ex);
                    }

                } catch (Exception e) {

                }
            } else {
                try {
                    MailDB mailDB = new MailDB();
                    String desc = Languages.instance(countryCode).getString("kichhoattkGiftcode");
                    mailDB.insert(uid, Languages.instance(countryCode).getString("hethong"), Languages.instance(countryCode).getString("dangky_kichhoat"), desc);
                    //insert mail users_mail
                    mailDB.insertUserMail(uid, 1);

                } catch (Exception e) {

                }
            }
            //end start insert mail

            return uid;

        } finally {
            con.close();
        }
    }

    public List<UserEntity> getTopEvent(long userId) throws Exception {
        List<UserEntity> res = new ArrayList<UserEntity>();
        String query = "{ call uspGetTopEvent(?) }";
        Connection con = DBPoolConnection.getConnection();
        try {

            CallableStatement cs = con.prepareCall(query);
            cs.clearParameters();
            cs.setLong(USER_ID_PARAM, userId);
            ResultSet rs = cs.executeQuery();
            if (rs != null) {

                while (rs.next()) {

                    UserEntity user = new UserEntity();

                    user.mUid = rs.getLong("UserID");
                    user.mUsername = rs.getString("Name");

                    user.money = rs.getLong("money");
                    user.avatarID = rs.getString("avatar");

                    res.add(user);

                }

                rs.close();
            }
        } finally {
            con.close();
        }
        return res;
    }

    public int creditMoney(String userName, long money, Date dt) throws SQLException {
        String query = "{?= call uspCreditMoney(?,?,?) }";
        Connection conn = DBPoolConnection.getConnection();
        int result = -1;

        try {
            CallableStatement cs = conn.prepareCall(query);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(USER_NAME_PARAM, userName);
            cs.setLong(MONEY_PARAM, money);
            cs.setTimestamp(DT_PARAM, new Timestamp(dt.getTime()));

            cs.execute();
            result = cs.getInt(1);
            return result;
        } finally {
            conn.close();
        }

    }

    public int freeTopup(long userId) throws SQLException {
        String query = "{?= call uspFreeTopup(?) }";
        Connection conn = DBPoolConnection.getConnection();
        int result = -1;

        try {
            CallableStatement cs = conn.prepareCall(query);
            cs.registerOutParameter(1, Types.INTEGER);

            cs.setLong(USER_ID_PARAM, userId);

            cs.execute();
            result = cs.getInt(1);
            return result;
        } finally {
            conn.close();
        }

    }

    public void setSocialAvatar(long uid, long fileId, int type) throws Exception {

        String query = "{call uspSetSocialAvatar(?,?,?) }";
        Connection conn = DBPoolConnection.getConnection();

        CallableStatement cs = conn.prepareCall(query);
        try {
            cs.clearParameters();
            cs.setLong(USER_ID_PARAM, uid);
            cs.setInt(TYPE_PARAM, type);
            cs.setLong(FILE_ID_PARAM, fileId);
            cs.execute();
            cs.close();
        } finally {
            conn.close();
        }

    }

    public void setStt(long uid, String stt) throws Exception {

        String query = "{call uspSetStt(?,?) }";
        Connection conn = DBPoolConnection.getConnection();

        CallableStatement cs = conn.prepareCall(query);
        try {
            cs.clearParameters();
            cs.setLong(USER_ID_PARAM, uid);
            cs.setString(STATUS_PARAM, stt);

            cs.execute();
            cs.close();
        } finally {
            conn.close();
        }

    }

    public List<UserEntity> getTopBlogger() throws Exception {
        List<UserEntity> res = new ArrayList<UserEntity>();
        String query = "{ call uspTopBlogger() }";
        Connection conn = DBPoolConnection.getConnection();
        try {

            CallableStatement cs = conn.prepareCall(query);
            cs.clearParameters();

            ResultSet rs = cs.executeQuery();
            if (rs != null) {

                while (rs.next()) {

                    UserEntity user = new UserEntity();

                    user.mUid = rs.getLong("UserID");
                    //System.out.println("======================BinhLT:" + res.mUid);
                    user.mUsername = rs.getString("Name");

                    user.avFileId = rs.getLong("avatarFileId");
                    user.stt = rs.getString("status");

                    res.add(user);

                }

                rs.close();
            }
        } finally {
            conn.close();
        }
        return res;
    }

    public UserInfoEntity getUserMxhInfo(long userId) throws Exception {
        UserInfoEntity res = null;
        String query = "{ call uspGetUserMXHInfo(?) }";
        Connection conn = DBPoolConnection.getConnection();
        try {

            CallableStatement cs = conn.prepareCall(query);
            cs.clearParameters();
            cs.setLong(USER_ID_PARAM, userId);

            ResultSet rs = cs.executeQuery();
            if (rs != null) {

                if (rs.next()) {
                    res = new UserInfoEntity();
                    res.address = rs.getString("address");
                    res.birthDay = rs.getDate("birthday");
                    //res.characterId = rs.getInt("characterId");
                    res.cityId = rs.getString("cityId");
                    res.hobby = rs.getString("hobby");
                    res.jobId = rs.getInt("jobId");
                    res.nickSkype = rs.getString("nickSkype");
                    res.nickYahoo = rs.getString("nickYahoo");

                }

                rs.close();
            }
        } finally {
            conn.close();
        }
        return res;
    }

    public List<UserEntity> getRichests(int partnerId) throws Exception {
        List<UserEntity> res = new Vector<UserEntity>(); //Track: udvUserInfo, TopRich storeprocedure
        String query = "{ call uspGetTopRich(?) }";
        Connection conn = DBPoolConnection.getConnection();
        try {

            CallableStatement cs = conn.prepareCall(query);
            cs.setInt(PARTNER_ID_PARAM, partnerId);

            ResultSet rs = cs.executeQuery();
            if (rs != null) {

                while (rs.next()) {

                    UserEntity user = new UserEntity();
                    user.mPassword = rs.getString("Password");
                    user.mUid = rs.getLong("UserID");
                    //System.out.println("======================BinhLT:" + res.mUid);
                    user.mUsername = rs.getString("Name");
                    user.mAge = rs.getInt("Age");
                    //res.lastLogin = rs.getDatetime("LastTime");
                    user.lastLogin = rs.getDate("LastTime");
                    user.mIsMale = rs.getInt("Sex") == 1;
                    user.lastMatch = rs.getLong("lastMatch");
                    user.avatarID = rs.getString("avatar");
                    user.level = rs.getInt("Level");
                    user.money = rs.getLong("Cash");
                    user.playsNumber = rs.getInt("WonPlaysNumber");
                    user.experience = rs.getInt("experience");

                    res.add(user);

                }

                rs.close();
            }
        } finally {
            conn.close();
        }

        return res;
    }

    public List<UserEntity> getBestPlayer(int partnerId) throws Exception {
        List<UserEntity> res = new Vector<UserEntity>();
        String query = "{ call uspGetTopLevel(?) }";
        Connection conn = DBPoolConnection.getConnection();
        try {

            CallableStatement cs = conn.prepareCall(query);
            cs.setInt(PARTNER_ID_PARAM, partnerId);

            ResultSet rs = cs.executeQuery();
            if (rs != null) {

                while (rs.next()) {

                    UserEntity user = new UserEntity();
                    user.mPassword = rs.getString("Password");
                    user.mUid = rs.getLong("UserID");
                    //System.out.println("======================BinhLT:" + res.mUid);
                    user.mUsername = rs.getString("Name");
                    user.mAge = rs.getInt("Age");
                    //res.lastLogin = rs.getDatetime("LastTime");
                    user.lastLogin = rs.getDate("LastTime");
                    user.mIsMale = rs.getInt("Sex") == 1;
                    user.lastMatch = rs.getLong("lastMatch");
                    user.avatarID = rs.getString("avatar");
                    user.level = rs.getInt("Level");
                    user.money = rs.getLong("Cash");
                    user.playsNumber = rs.getInt("WonPlaysNumber");
                    user.experience = rs.getInt("experience");

                    res.add(user);

                }

                rs.close();
            }
        } finally {
            conn.close();
        }
        return res;
    }

    public UserInfoEntity getUserMxhAccount(long userId) throws SQLException {
        UserInfoEntity res = null;
        String query = "{ call uspGetUserMXHInfo(?) }";
        Connection conn = DBPoolConnection.getConnection();
        try {

            CallableStatement cs = conn.prepareCall(query);
            cs.clearParameters();
            cs.setLong(USER_ID_PARAM, userId);

            ResultSet rs = cs.executeQuery();
            if (rs != null) {

                if (rs.next()) {
                    res = new UserInfoEntity();
                    res.address = rs.getString("address");
                    res.birthDay = rs.getDate("birthday");
                    //res.characterId = rs.getInt("characterId");
                    res.cityId = rs.getString("cityId");
                    res.hobby = rs.getString("hobby");
                    res.jobId = rs.getInt("jobId");
                    res.nickSkype = rs.getString("nickSkype");
                    res.nickYahoo = rs.getString("nickYahoo");

                }

                rs.close();
            }
        } catch (Throwable e) {
            res = new UserInfoEntity();
            res.address = "Đang cập nhật";
            res.birthDay = null;
            //res.characterId = 0;
            res.cityId = "";
            res.hobby = "Đang cập nhật";
            res.jobId = 0;
            res.nickSkype = "Đang cập nhật";
            res.nickYahoo = "Đang cập nhật";
        } finally {
            conn.close();
        }
        return res;
    }

    public int useIAP(long userId, String productId) throws SQLException {

        String query = "{?= call uspUseIAP(?,?) }";
        Connection conn = DBPoolConnection.getConnection();
        int ret = -1;
        try {
            CallableStatement cs = conn.prepareCall(query);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setLong(USER_ID_PARAM, userId);
            cs.setString("productId", productId);
            cs.execute();
            ret = cs.getInt(1);
            cs.close();
        } finally {
            conn.close();
        }
        return ret;
    }

    public int useGiftcode(long userId, String serial, int type) throws SQLException {
        String query = "{?= call uspUseGiftCode(?,?,?) }";

        Connection conn = DBPoolConnection.getConnection();
        int ret = -1;
        try {
            CallableStatement cs = conn.prepareCall(query);
            cs.registerOutParameter(1, Types.INTEGER);

            cs.setLong(USER_ID_PARAM, userId);
            cs.setInt(DEVICE_TYPE_PARAM, type);
            cs.setString(SERIAL_PARAM, serial);

            cs.execute();

            ret = cs.getInt(1);

            cs.close();
        } finally {
            conn.close();
        }

        return ret;

    }

    public long bonusMoney(long userId, long money, int logTypeId) throws SQLException {
        String query = "{call uspBonusMoneyCC(?, ?, ?) }";
        Connection conn = DBPoolConnection.getConnection();
        long result = -1;

        try {
            CallableStatement cs = conn.prepareCall(query);
//                cs.registerOutParameter(1, Types.INTEGER );

            cs.setLong(USER_ID_PARAM, userId);
            cs.setLong(MONEY_PARAM, money);
            cs.setInt(LOG_TYPE_ID_PARAM, logTypeId);

            ResultSet rs = cs.executeQuery();
            if (rs != null && rs.next()) {
                result = rs.getLong("cash");
            }

            return result;
        } finally {
            conn.close();
        }

    }

    private static List<UserEntity> getBotUser(String isRealMoney) throws SQLException {

        List<UserEntity> res = new ArrayList<UserEntity>();
        String query = "SELECT * from users where utype=9 and realmoney > 5000 order by uid asc limit 50"; //utype = bot 

        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {

            conn = DBGame.instance().getConnection();
            stm = conn.prepareStatement(query);
            rs = stm.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    UserEntity user = new UserEntity();

                    user.mUid = rs.getLong("uid");

                    user.mUsername = rs.getString("username");
                    user.viewName = rs.getString("viewname");

                    user.lastLogin = rs.getTimestamp("created");
                    user.mIsMale = rs.getBoolean("sex");

                    user.avatarID = rs.getString("avatar");
                    user.level = rs.getInt("level");

                    user.money = rs.getLong(isRealMoney);

                    user.playsNumber = rs.getInt("total");

                    user.cellPhone = "xxxx";
                    user.isActive = rs.getBoolean("active");
                    user.experience = rs.getInt("exp");

                    user.isOnline = false;
                    user.vipId = rs.getInt("level");

                    user.partnerId = 1;
                    user.stt = rs.getString("status");

                    if (rs.getInt("utype") == 6) {
                        user.isLock = 1;
                    }

                    user.vipName = "Vip ";
                    user.groupId = rs.getInt("groupId");
                    res.add(user);
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

    public List<AlertUserEntity> getAlertUser(long userId) throws SQLException {
        List<AlertUserEntity> res = new ArrayList<AlertUserEntity>(); //Track: udvUserInfo, TopRich storeprocedure

        Connection conn = null;

        PreparedStatement cs = null;
        PreparedStatement stm_update = null;
        ResultSet rs = null;

        try {
            conn = DBVip.instance().getConnection();

            cs = conn.prepareStatement("SELECT * FROM alertuser where uid=? and dateSend is null");
            cs.setLong(1, userId);

            rs = cs.executeQuery();

            if (rs != null) {

                while (rs.next()) {
                    String content = rs.getString("content");

                    long id = rs.getLong("id");
                    int type = rs.getInt("type");
                    AlertUserEntity entity = new AlertUserEntity(content, userId, type);
                    res.add(entity);
                    //update 
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date2 = new Date();
                    String now = dateFormat.format(date2);
                    stm_update = conn.prepareStatement("UPDATE alertuser SET dateSend=?  WHERE uid=? and id = ?");
                    stm_update.setString(1, now);
                    stm_update.setLong(2, userId);
                    stm_update.setLong(3, id);
                    stm_update.executeUpdate();
                }
                rs.close();

            }

        } finally {
            if (conn != null) {
                conn.close();
            }
            if (cs != null) {
                cs.close();
            }
            if (stm_update != null) {
                stm_update.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return res;
    }

    public List<UserEntity> getTopGame(int partnerId, int gameId) throws Exception {
        List<UserEntity> res = new ArrayList<UserEntity>();
        String query = "{ call uspGetTopLevel(?) }";
        Connection conn = DBPoolConnection.getConnection();
        try {

            CallableStatement cs = conn.prepareCall(query);
            cs.setInt(PARTNER_ID_PARAM, partnerId);

            ResultSet rs = cs.executeQuery();
            if (rs != null) {

                while (rs.next()) {

                    UserEntity user = new UserEntity();

                    user.mUid = rs.getLong("UserID");
                    //System.out.println("======================BinhLT:" + res.mUid);
                    user.mUsername = rs.getString("Name");

                    user.playsNumber = rs.getInt("times"); //so lan tra loi cau hoi altp
                    user.point = rs.getInt("question"); //tra loi den cau so may

                    res.add(user);

                }

                rs.close();
            }
        } finally {
            conn.close();
        }
        return res;
    }

    public long auditUser(long userId,
            String mobileVersion, String screen, String ip, String deviceId) throws SQLException {

        long resultDevice = 0;
//        String query = "{ call uspAuditUser(?, ?, ?, ?, ?) }";
//        Connection con = DBPoolConnection.getConnection();
//        ResultSet rs = null;
//        CallableStatement cs = null;
//        try {
//
//            cs = con.prepareCall(query);
////            cs.clearParameters();
//
//            cs.setLong(USER_ID_PARAM, userId);
//            cs.setString(SCREEN_PARAM, null);
//            cs.setString(MOBILE_VERSION_PARAM, mobileVersion);
//            cs.setString(IP_PARAM, ip);
//            cs.setLong(DEVICE_ID_PARAM, deviceId);
//
//            rs = cs.executeQuery();
//
//            if (rs != null && rs.next()) {
//                resultDevice = rs.getLong("deviceId");
//                rs.close();
//
//            }
//            cs.clearParameters();
//            cs.close();
//
//        } catch (Throwable ex) {
//
//            cs.close();
//
//        } finally {
//
//            con.close();
//        }
        return resultDevice;

    }

    public static List<UserEntity> getRankPlayer(int type, String isRealMoney) throws Exception {
        List<UserEntity> res = new ArrayList<UserEntity>();
        String orderBy = isRealMoney;
        if (type == 2) {
            orderBy = "win";
        } else if (type == 3) {
            orderBy = "exp";
        } else if (type == 4) {
            orderBy = "total";//tổng số lần chơi
        }

        String query = "SELECT * from users where utype=0 order by " + orderBy + " desc limit 10";

        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {

            conn = DBGame.instance().getConnection();
            stm = conn.prepareStatement(query);
            rs = stm.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    UserEntity user = new UserEntity();

                    user.mUid = rs.getLong("uid");

                    user.mUsername = rs.getString("username");
                    user.viewName = rs.getString("viewname");
                    user.avatarID = rs.getString("avatar");
                    user.rank = rs.getLong(orderBy);
                    res.add(user);
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

    public List<GameDataEntity> getGameData(long uid) throws SQLException {

        List<GameDataEntity> data = new ArrayList<GameDataEntity>();
        //tam bo ko dung 
        String query = "SELECT zoneId, win, lost, expr,totalplay FROM gamedata where userId = ?";
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBVip.instance().getConnection();
            cs = con.prepareCall(query);
            cs.setLong(1, uid);
            rs = cs.executeQuery();
            if (rs != null) {
                while (rs.next()) {

                    GameDataEntity entity = new GameDataEntity();
                    entity.setZoneID(rs.getInt(1));
                    entity.setWin(rs.getInt(2));
                    entity.setLost(rs.getInt(3));
                    entity.setExpr(rs.getInt(4));
                    entity.setTotalplay(rs.getInt(5));

                    data.add(entity);
                }

            }

        } catch (Exception ex) {
            mLog.equals(" Game data get error " + ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }

            if (cs != null) {
                cs.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return data;
    }

    //add by zep
    public int checkDeviceID(String deviceID) {
        int result = 0;
        long start = System.currentTimeMillis();
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = DBGame.instance().getConnection();
            stm = conn.prepareStatement("SELECT count(deviceID) as total FROM users WHERE deviceID=?");
            stm.setString(1, deviceID);
            rs = stm.executeQuery();
            long count = System.currentTimeMillis() - start;
            if (count > 100L) {

            }

            if (rs.first()) {

                result = rs.getInt("total");

            }

        } catch (SQLException e) {

        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (stm != null) {
                    stm.close();
                }
                if (rs != null) {
                    rs.close();
                }

            } catch (SQLException sqle) {
            }
        }
        return result;
    }

    public int countByIP(String ip) {
        int result = 0;
        long start = System.currentTimeMillis();
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = DBGame.instance().getConnection();
            stm = conn.prepareStatement("SELECT count(ip) as total FROM users WHERE ip=?");
            stm.setString(1, ip);
            rs = stm.executeQuery();
            long count = System.currentTimeMillis() - start;
            if (count > 100L) {

            }

            if (rs.first()) {

                result = rs.getInt("total");

            }

        } catch (SQLException e) {

        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (stm != null) {
                    stm.close();
                }
                if (rs != null) {
                    rs.close();
                }

            } catch (SQLException sqle) {
            }
        }
        return result;
    }

    public Phone getPhone(long uid, String username) throws SQLException {
        Phone res = null;

        //String query = "{ call uspGetUserInfoByUid(?) }";
        StringBuffer query = new StringBuffer();
//        query.append("select userId,password,name,viewname,lastLogin,avatarId,level,cash,money,wonPlaysNumber,experience,address,avatarFileId,");
//        query.append("biaFileId,vipId,isActive,partnerId,phoneNumber,refCode,status,cmt FROM workinguser where userId=? ");
        query.append("select * from phone where uid = ?");
        Connection con = null;
        PreparedStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBGame.instance().getConnection();
            //CallableStatement cs = con.prepareCall(query);
            cs = con.prepareStatement(query.toString());
            cs.setLong(1, uid);
            rs = cs.executeQuery();

            if (rs != null && rs.next()) {
                res = new Phone();
                res.id = rs.getLong("id");
                res.uid = rs.getLong("uid");

                //System.out.println("rs.getString(\"avatar\"):"+rs.getString("avatar"));
                res.phone = rs.getString("phone");
                res.activeCode = rs.getString("activeCode");
                res.numActive = rs.getInt("num_active");

            }
            if (res == null) {
                String queryInsert = "INSERT INTO phone (uid, num_active, activeCode) VALUES (? , ? , ?)";

                Random random = new Random();
                String activeCode = username + "_" + (100000 + random.nextInt(1000000));
                res = new Phone();
                res.uid = uid;
                res.numActive = 0;
                res.activeCode = activeCode;
                PreparedStatement stm = null;

                int now = (int) (System.currentTimeMillis() / 1000L);

                stm = con.prepareStatement(queryInsert, 1);
                stm.setLong(1, res.uid);
                stm.setInt(2, res.numActive);
                stm.setString(3, res.activeCode);

                stm.executeUpdate();

            }

        } finally {
            if (con != null) {
                con.close();// close init connection
            }
            if (rs != null) {
                rs.close();// close init connection
            }
            if (cs != null) {
                cs.close();// close init connection
            }

        }
        return res;
    }

    public int getExpUpByZoneId (int logTypeId, boolean isWin) {
        int zoneId = logTypeId - SimpleTable.LOG_TYPE_GAME_START;
        if (zoneId == ZoneID.BIDA || zoneId == ZoneID.BIDA_FOUR || zoneId == ZoneID.BIDA_PHOM || zoneId == ZoneID.COTUONG) {
            zoneId = logTypeId;
        }
        if (isWin) {
            switch (zoneId) {
                case ZoneID.NEW_BA_CAY:
                    return 5;
                case ZoneID.SHOOTS:
                    return 5;
                case ZoneID.DRAGGER:
                    return 5;
                case ZoneID.POKER:
                    return 5;
            }
            return 10;
        } else {
            switch (zoneId) {
                case ZoneID.NEW_BA_CAY:
                    return 3;
                case ZoneID.SHOOTS:
                    return 3;
                case ZoneID.DRAGGER:
                    return 3;
                case ZoneID.POKER:
                    return 3;
            }
            return 5;
        }
    }    

    public static void main(String[] args) throws SQLException {
//         Random random =new Random();
//        String activeCode=  "alskdf_" +(10000+random.nextInt(100000));
//        for(int i= 0;i<200;i++){
//            System.out.println(random.nextInt(100000));
//        }
//        int a=1;

        UserDB udb = new UserDB();
        Phone p = udb.getPhone(1233, "popeye");
        int a = 1;
    }
}
