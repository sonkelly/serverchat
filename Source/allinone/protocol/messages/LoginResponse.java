package allinone.protocol.messages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.ChargingInfo;
import allinone.data.SimpleTable;
import allinone.data.UserEntity;
import allinone.data.UserInfoEntity;
import allinone.data.VersionEntity;
//import bacay.data.VersionEntity;
import allinone.room.NRoomEntity;

public class LoginResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public long mUid;
    public long money;
    public long realmoney;
    public String avatarID = "ava-1.jpg";
    public int level = 1;
    //public Vector<AdvEntity> advs;
    public boolean isNewProtocol;
//    public VersionEntity lVersion;
    public Date lastLogin;
    public String TuocVi;
    public String avatarVerion;
    public int playNumber;
    public long moneyUpdateLevel;
//    public boolean isByteProtocol;
    public String smsNumber = "8700";
    public String smsContent = "VUI";
    public String smsMessage = "Bạn đã nạp tiền thành công. Tài khoản của bạn sẽ được công thêm 15k tiền.";

    public String linkDown = "";
    public String newVer = "";

    public long lastRoom = 0;
    public int zone_id;
    public String lastRoomName = "";

    public VersionEntity version;
    public boolean isMobile = true;
    //public String avatar;
    public String cellPhone;

    //charging info
    public List<ChargingInfo> chargingInfo = new ArrayList<ChargingInfo>();
    public List<NRoomEntity> lstRooms = null;
    public List<SimpleTable> lstTables = null;
    public int newZoneId;
    public String active;
    public boolean isMxh;
    public UserEntity usrEntity;
    public boolean isNeedUpdate = false;

    public String deviceId;//ID device
    public String mobileVersion;
    public int expr = 0;
    public boolean isActive = false;
    public boolean isBonus = false;

    public int numberOnline = 0;

    // check email
    public String mail  = "";
    public boolean chkEmail = false;
    public boolean chkEvent = false;
    public String alertEmailContent = "";
    public String alertEmailTitle = "";
    public boolean isPhoneUpdate = false;
    public String viewname = "";
    public boolean isPayment = true;
    public boolean isUploadAvatar = false;
    public int newDateLogin = 0;
    public UserInfoEntity usrInfoEntity = null;
    
    public String activeCode;
    
    
    public void setLastRoom(long l, String r, int z) {
        lastRoom = l;
        lastRoomName = r;
        zone_id = z;
    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public void setSuccess(int aCode, long aUid, long mn, String avatar, int lev, Date time, String Tuocvi, int playNumbers, long updatelevel, String _viewname, long _realmoney) {
        mCode = aCode;
        mUid = aUid;
        money = mn;
        avatarID = avatar;
        level = lev;
        lastLogin = time;
        TuocVi = Tuocvi;
        playNumber = playNumbers;
        moneyUpdateLevel = updatelevel;
        viewname = _viewname;
        realmoney = _realmoney;
    }

    public IResponseMessage createNew() {
        return new LoginResponse();
    }
}
