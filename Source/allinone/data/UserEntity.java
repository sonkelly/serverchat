package allinone.data;

import java.io.Serializable;
import java.util.Date;

public class UserEntity implements Serializable {

    public boolean lockRequest = false;
    public long mUid;
    public String mUsername;
    public String viewName;
    public String mPassword;
    public int mAge;
    public boolean mIsMale;
    //public long cash;
    public long money;
    public long realmoney;

    public int level;
    public String avatarID;
    public int playsNumber;
    public boolean isLogin = false;
    public Date lastLogin;
    public long lastMatch;
    public String avatarM;
    public String avatarVersion;
    public String avatarF;
    //public String TuocVi;
    public String cellPhone;
    public int experience;
    public String vipName;
    public int vipId;
    public boolean isOnline = false;

    public int point;//for euro
    public int bonus;//for euro

    public long avFileId;
    public long biaFileId;
    public String stt;
    public boolean hasBia;
    public int partnerId;
    public boolean isActive;

    public int glasses;
    public int shirt;
    public int jeans;
    public int hair;

    public int timesQuay;

    public int refCode;

    public String name;
    public String cmt;
    public String address;
    public String mail;

    public long rank;

    public UserInfoEntity usrInfoEntity;

    public int isLock = 0;
    public int newDateLogin = 0;
    //add by zep
    public int groupId = 0;
    public int utype = 0;

    public UserEntity() {
    }

    public void refreshStatus() {

        java.util.Date utilNow = new java.util.Date();
        java.sql.Date now = new java.sql.Date(utilNow.getYear(), utilNow.getMonth(), utilNow.getDate());

        if (this.lastLogin != null) {
            int comparison = now.compareTo(this.lastLogin);

            if (comparison == 0) {
                this.newDateLogin = 0;
            } else if (comparison > 0) {
                this.newDateLogin = 1;
            }
        }

    }
}
