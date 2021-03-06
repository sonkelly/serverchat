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
import allinone.room.NRoomEntity;

public class PlaySocialLoginResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public long mUid;
    public long money;
    public long realmoney;
    public String avatarID;
    public int level;
    public boolean isNewProtocol;
    public Date lastLogin;
    public String TuocVi;
    public String avatarVerion;
    public int playNumber;
    public long moneyUpdateLevel;

    public String linkDown = "";
    public String newVer = "";

    public long lastRoom = -1;
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

    public String deviceUId;
    public String mobileVersion;

    public int expr = 0;
    public boolean isActive = false;
    public boolean isBonus = false;
    public String password = "";

    public int numberOnline = 0;
    public boolean isPhoneUpdate = false;

    // check email
    public boolean chkEmail = false;
    public boolean chkEvent = false;
    public String alertEmailContent = "";
    public String alertEmailTitle = "";
    public String viewname = "";
    public boolean isPayment = true;
    public boolean isUploadAvatar = false;
    public String mail = "";
    public int newDateLogin = 0;
    public UserInfoEntity usrInfoEntity = null;
    public String activeCode;
    
    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public void setSuccess(int aCode, long aUid, long mn,long rm, Date time, String _avatar, String _viewname, int _level) {
        mCode = aCode;
        mUid = aUid;
        money = mn;
        lastLogin = time;
        avatarID = _avatar;
        viewname = _viewname;
        level = _level;
        realmoney = rm;

    }

    public void setLastRoom(long l, String r, int z) {
        lastRoom = l;
        lastRoomName = r;
        zone_id = z;
    }

    public IResponseMessage createNew() {
        return new PlaySocialLoginResponse();
    }
}
