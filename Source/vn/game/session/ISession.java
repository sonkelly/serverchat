package vn.game.session;

import java.util.Date;
import java.util.UUID;
import java.util.Vector;

import vn.game.common.ServerException;
import vn.game.protocol.BusinessProperties;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.Room;
import vn.game.room.Zone;
import vn.game.room.ZoneManager;
import allinone.business.BusinessException;
import allinone.data.FindFriendEntity;
import allinone.data.InviteEntity;
import allinone.data.PositionEntity;
import allinone.data.SimpleTable;
import allinone.data.UploadAvatarEntity;
import allinone.data.UserEntity;
import allinone.protocol.messages.LoginRequest;
import allinone.tournement.TourManager;

import com.allinone.vivu.Area;
import com.allinone.vivu.Building;
import com.allinone.vivu.City;
import com.allinone.vivu.CityManager;
import com.allinone.vivu.FuckingPlayer;
import com.allinone.vivu.Group;

public abstract interface ISession {

    public static final long UNSPECIFIED_ID = 0L;
    // BINHLT
    public abstract SimpleTable getTable();

    public abstract void setTable(SimpleTable table);

    public abstract boolean getVersion35();

    public abstract void setVersion35(boolean is);

    public abstract void cancelTable();

    public abstract void setCurrentZone(int zoneID);

    public abstract int getCurrentZone();

    public abstract int getByteProtocol();

    public abstract void setByteProtocol(int version);

    public abstract void setRoomID(int id);

    public abstract int getRoomID();

    public abstract Room getRoom();

    public abstract void setRoom(Room room);

    public abstract int getRoomLevel();

    public abstract void setRoomLevel(int roomLevel);

    public abstract int getPhongID();

    public abstract void setPhongID(int phongID);

    public abstract void addAvatarString(String temp);

    public abstract String getAvatarString();

    public abstract void setAvatarNull();
    // END

    public abstract void setIP(String ip);

    public abstract String getIP();

    public abstract String getID();

    public abstract void setUID(Long paramLong);

    public abstract void setUIDNull();

    public abstract Long getUID();

    public abstract void setUserName(String paramString);

    public abstract String getUserName();

    public abstract void close();

    public abstract SessionManager getManager();

    public abstract void setBusinessProperties(
            BusinessProperties paramBusinessProperties);

    public abstract BusinessProperties getBusinessProperties();

    public abstract void sessionClosed();

    public abstract boolean isClosed();

    public abstract void setScreenSize(String screen);

    public abstract void setMobile(String ver);

    public abstract void ping(ISession i);

    public abstract void receiveMessage();

    public abstract boolean getMobile();

    public abstract String getMobileVer();

    public abstract void sessionCreated(Object paramObject);

    public abstract Object getProtocolOutput();

    public abstract boolean write(Object paramObject) throws ServerException;

    public abstract String userInfo();

    public abstract boolean isDirect();

    //public abstract boolean write() throws ServerException;
    public abstract IResponsePackage getDirectMessages();

    public abstract void setIsHandling(Boolean paramBoolean);

    public abstract boolean isHandling();

    public abstract String getCookies();

    public abstract void setCookies(String paramString);

    public abstract MessageFactory getMessageFactory();

    public abstract boolean realDead();

    public abstract void setMessageFactory(MessageFactory paramMessageFactory);

    public abstract String getPackageFormat();

    public abstract void setPackageFormat(String paramString);

    public abstract Date getCreatedTime();

    public abstract void setCreatedTime(Date paramDate);

    public abstract Date getLastAccessTime();

    public abstract void setLastAccessTime(Date paramDate);

    public abstract void setTimeout(Integer paramInteger);

    public abstract boolean isExpired();

    public abstract boolean isSpam();

    public abstract void setSpam(boolean is);

    public abstract boolean realExpired();

    public abstract long getLastMessage();

    public abstract void setLoggedIn(Boolean paramBoolean);

    public abstract boolean isLoggedIn();

    // public abstract void setCurrentDBConnection(IConnection
    // paramIConnection);
    // public abstract IConnection getCurrentDBConnection();
    public abstract void setCommit(boolean paramBoolean);

    public abstract boolean isCommit();

    public abstract void joinedRoom(Room paramRoom);

    public abstract Room findJoinedRoom(long paramLong);

    public abstract boolean isJoinedFull(int paramInt);

    public abstract Room leftRoom(long paramLong);

    public abstract Vector<Room> getJoinedRooms();

    public abstract void setZoneManager(ZoneManager paramZoneManager);

    public abstract void setTourMgr(TourManager tourMgr);

    public abstract TourManager getTourMgr();

    public abstract Zone findZone(int paramInt);

    public abstract StringBuilder getCollectInfo();

    public abstract long getLastFastMatch();

    public abstract void setLastFastMatch(long lastFastMatchId);

    public abstract boolean isMobileDevice();

    public void setMobileDevice(boolean mobileDevice);

    public abstract Building getBuilding();

    public abstract void setBuilding(Building b);

    public abstract void setBuilding(int b) throws BusinessException;

    public abstract Group getGroup();

    public abstract CityManager getCityManage();

    public abstract City getCity();

    public abstract Area getSubZone();

    public abstract void setmGroup(Group mGroup) throws BusinessException;

    public abstract void setmGroup(int group) throws BusinessException;

    public abstract void setmSubZone(Area mSubZone) throws BusinessException;

    public abstract void setmSubZone(int zone) throws BusinessException;

    public abstract void setmCityManager(CityManager mCityManager);

    public abstract void setmCity(City mCity);

    public abstract void setmCity(int idCity) throws BusinessException;

    public abstract void broadcast(Object obj, int type) throws ServerException, BusinessException;

    public abstract FuckingPlayer getUser() throws BusinessException;

    public abstract void setUser(FuckingPlayer u) throws BusinessException;

    public abstract int getChatRoom();

    public abstract void setChatRoom(int chatRoomId);

    public abstract boolean isUploadAvatar();

    public abstract void setUploadAvatar(boolean isUpload);

    public abstract void setHide(boolean b);

    public abstract boolean isHidden();

    public abstract void setMXHDevice(boolean b);

    public abstract boolean isMXHDevice();

    public abstract UserEntity getUserEntity();

    public abstract void setUserEntity(UserEntity entity);

    public abstract void setAcceptInvite(boolean b);

    public abstract boolean isAcceptInvite();

    public abstract void setInviteEntity(InviteEntity entity);

    public abstract InviteEntity getInviteEntity();

    public abstract void setRejectInvite(boolean b);

    public abstract boolean isRejectInvite();

    public abstract void setLastFP(long lastFP);

    public abstract long getLastFP();

    public abstract void setLastRegister(long lastRegister);

    public abstract long getLastRegister();

    public abstract String getOnlyIP();

    public abstract void setReplyInvite(boolean b);

    public abstract boolean isReplyInvite();

    public abstract int getNumInvite();

    public abstract void setNumInvite(int numInvite);

    public abstract FindFriendEntity getFindFriendCache();

    public abstract void setFindFriendCache(FindFriendEntity entity);

    public abstract UploadAvatarEntity getUploadAvatarEntity();

    public abstract void setUploadAvatarEntity(UploadAvatarEntity entity);

    public abstract byte[] getUploadBytePart();

    public abstract void setUploadBytePart(byte[] part);

    public abstract PositionEntity getCurrPosition();

    public abstract void setCurrPosition(PositionEntity position);

    public abstract int getRetryLogin(String userName);

    public abstract void setRetryLogin(String userName);

    public abstract int getBotType();

    public abstract void setBotType(int botType);

    public abstract long getLastSendImage();

    public abstract void setLastSendImage(long lastSendImage);

    public abstract int getDeviceType();

    public abstract void setDeviceType(int deviceType);

    public abstract UUID getImageRequest();

    public abstract void setImageRequest(UUID uuid);

    public abstract void setLastUID(long uuid);

    public abstract long getLastUID();

    public abstract void setTopplayVersion(int topplayVersion);

    public abstract int getTopplayVersion();
    //End
    // BINHLT
    //add by zep

    public abstract void setUserManager(LoginRequest entity);

    public abstract LoginRequest getUserManager();
    //xu dung cho tai xiu
    public abstract void setTaiXiuDayThang(long daythang);
    public abstract void setTaiXiuDayThua(long daythang);
    public abstract long getTaiXiuDayThang();
    public abstract long getTaiXiuDayThua();
    public abstract void setTaiXiuTotalBet(long money);
    public abstract long getTaiXiuTotalBet();
    
    
    //dung cho ko setLastAccessTime
    public abstract boolean setIsSetLastAccessTime(boolean is);
    public abstract boolean getIsSetLastAccessTime();
    
    public abstract void setuType(int is);
    public abstract int getuType();
    
    public abstract void setWaitRecharge(int is);
    public abstract int getWaitRecharge();
    
    public abstract void setRealMoney(String is);
    public abstract String getRealMoney();
    //end add by zep
    public abstract void setIsRealMoney(boolean is);
    public abstract boolean getIsRealMoney();
}
