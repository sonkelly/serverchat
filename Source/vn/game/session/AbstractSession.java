package vn.game.session;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.slf4j.Logger;

import vn.game.bytebuffer.IByteBuffer;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.BusinessProperties;
import vn.game.protocol.IBusiness;
import vn.game.protocol.IPackageProtocol;
import vn.game.protocol.IResponseMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.protocol.SimpleResponsePackage;
import vn.game.room.Phong;
import vn.game.room.Room;
import vn.game.room.Zone;
import vn.game.room.ZoneManager;
import allinone.business.BusinessException;
import allinone.data.FindFriendEntity;
import allinone.data.InviteEntity;
import allinone.data.MessagesID;
import allinone.data.PositionEntity;
import allinone.data.RetryLoginEntity;
import allinone.data.SimpleTable;
import allinone.data.UploadAvatarEntity;
import allinone.data.UserEntity;
import allinone.protocol.messages.CancelRequest;
import allinone.protocol.messages.KeepConnectionResponse;
import allinone.protocol.messages.LoginRequest;
import allinone.tournement.TourManager;

import com.allinone.vivu.Area;
import com.allinone.vivu.Building;
import com.allinone.vivu.City;
import com.allinone.vivu.CityManager;
import com.allinone.vivu.FuckingPlayer;
import com.allinone.vivu.Group;

public abstract class AbstractSession implements ISession {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(AbstractSession.class);
    @SuppressWarnings("unused")
    private final String SESSION_ID = "session.id";
    @SuppressWarnings("unused")
    private final String SESSION_USER_ID = "session.user.id";
    @SuppressWarnings("unused")
    private final String SESSION_USER_NAME = "session.user.name";
    @SuppressWarnings("unused")
    private final String SESSION_BUSINESS_PROPS = "session.business.props";
    @SuppressWarnings("unused")
    private final String SESSION_IS_HANDLING = "session.is.handling";
    @SuppressWarnings("unused")
    private final String SESSION_ATTACHTMENT_OBJECT = "session.attachment.object";
    @SuppressWarnings("unused")
    private final String SESSION_COOKIES = "session.cookies";
    @SuppressWarnings("unused")
    private final String SESSION_MESSAGE_FACTORY = "session.message.factory";
    @SuppressWarnings("unused")
    private final String SESSION_PKG_FORMAT = "session.package.format";
    @SuppressWarnings("unused")
    private final String SESSION_CREATED_TIME = "session.created.time";
    @SuppressWarnings("unused")
    private final String SESSION_LASTACCESS_TIME = "session.lastaccess.time";
    @SuppressWarnings("unused")
    private final String SESSION_TIMEOUT = "session.timeout";
    @SuppressWarnings("unused")
    private final String SESSION_LOGGED_IN = "session.logged.in";
    @SuppressWarnings("unused")
    private final String SESSION_CURRENT_DB_CONNECTION = "session.current.db.connection";
    @SuppressWarnings("unused")
    private final String SESSION_IS_COMMIT = "session.is.commit";
    private ConcurrentHashMap<String, Object> mAttrs;
    private final IResponsePackage mResPkg;
    private boolean mIsClosed = true;
    private SessionManager mSessionMgr;
    private ZoneManager mZoneMgr;
    private final ConcurrentHashMap<Long, Room> mJoinedRooms;
    private boolean isMobile = false;
    private static final int DEAD_TIMEOUT = 300000;
    private String mobileVersion = "";
    private String screenSize = "";
    private StringBuilder collectInfo;
    private Room room;
    private long lastFastMatchId;
    private int protocolVersion;
    private boolean mobileDevice;
    private int chatRoomId;
    private boolean uploadAvatar;
    private long lastRegister = 0;
    private UserEntity userEntity;
    private String ip;
    private int topplayVersion = 0;
    // For websocket
    private boolean isWeb = false;
    //add by zep
    private LoginRequest userManager;
    private long txDayThang = 0L;
    private long txDayThua = 0L;
    private long txTotalBet = 0L;
    private boolean setLastAccessTime = true;
    private int uType = 0;
    private int isWaitRecharge = 0;
    private String isRealMoney = "money";
    private boolean isBRealMoney = true;
    //end add by zep

    @Override
    public String getOnlyIP() {
        return ip;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public AbstractSession(boolean ws) {
        this.mAttrs = new ConcurrentHashMap();
        this.mResPkg = new SimpleResponsePackage();
        this.mJoinedRooms = new ConcurrentHashMap();
        this.mIsClosed = true;
        collectInfo = new StringBuilder();
        this.isWeb = ws;
    }

    public boolean isWebsocket() {
        return isWeb;
    }
    // BINHLT - for new Zone
    private Group mGroup;
    private Area mSubZone;
    private City mCity;
    private CityManager mCityManager;
    private Building mBuilding;
    private FuckingPlayer user;
    private SimpleTable table;

    @Override
    public SimpleTable getTable() {
        return table;
    }

    @Override
    public void setTable(SimpleTable table) {
        this.table = table;
    }

    @Override
    public FuckingPlayer getUser() throws BusinessException {
        return this.user;
    }

    @Override
    public void setUser(FuckingPlayer u) throws BusinessException {
        this.user = u;
    }

    @Override
    public void broadcast(Object obj, int type) throws ServerException, BusinessException {
        switch (type) {
            case 0:
                broadcastGroup(obj);
                break;
            case 1:
                broadcastArea(obj);
                break;
            case 2:
                broadcastCity(obj);
                break;
            default:
                break;
        }
    }

    private void broadcastCity(Object obj) throws ServerException, BusinessException {
        this.mCity.broadcast(obj, this.getUID());
    }

    private void broadcastArea(Object obj) throws ServerException, BusinessException {
        this.mSubZone.broadcast(obj, this.getUID());
    }

    private void broadcastGroup(Object obj) throws ServerException, BusinessException {
        this.mGroup.broadcast(obj, false, this.getUID());
    }

    @Override
    public void setBuilding(Building b) {
        mBuilding = b;
    }

    @Override
    public void setBuilding(int b) throws BusinessException {
        // setBuilding(mSubZone.getBuilding(b));
    }

    @Override
    public Building getBuilding() {
        return mBuilding;
    }

    @Override
    public City getCity() {
        return mCity;
    }

    @Override
    public CityManager getCityManage() {
        return mCityManager;
    }

    @Override
    public Group getGroup() {
        return this.mGroup;
    }

    @Override
    public Area getSubZone() {
        return mSubZone;
    }

    @Override
    public void setmCity(City mCity) {
        this.mCity = mCity;
    }

    @Override
    public void setmCity(int idCity) throws BusinessException {
        City c = mCityManager.getCity(idCity);
        this.mCity = c;
    }

    @Override
    public void setmGroup(Group mGroup) throws BusinessException {
        this.mGroup = mGroup;
        mGroup.join(this);
    }

    @Override
    public void setmGroup(int group) throws BusinessException {
        Group g = this.mSubZone.getGroup(group);
        setmGroup(g);
        // g.join(this);
    }

    @Override
    public void setmSubZone(Area mSubZone) throws BusinessException {
        this.mSubZone = mSubZone;
        if (mSubZone != null) {
            mSubZone.join(this);
        }
    }

    @Override
    public void setmSubZone(int zone) throws BusinessException {

        Area sZone = mCity.getSubZone(zone);
        setmSubZone(sZone);

    }

    @Override
    public void setmCityManager(CityManager mCityManager) {
        this.mCityManager = mCityManager;
    }

    private boolean version35 = false;

    @Override
    public boolean getVersion35() {
        return version35;
    }

    @Override
    public void setVersion35(boolean is) {
        version35 = is;
    }

    private TourManager tourMgr;

    @Override
    public void setTourMgr(TourManager tourMgr) {
        this.tourMgr = tourMgr;
    }

    @Override
    public TourManager getTourMgr() {
        return tourMgr;
    }

    // BINHLT - Cancel Message while disconnected.
    @Override
    public void cancelTable() {
        IResponsePackage responsePkg = this.getDirectMessages();// new
        // SimpleResponsePackage();

        MessageFactory msgFactory = this.getMessageFactory();
        IBusiness business;
        long uid = this.getUID();
        Zone zone = this.findZone(this.getCurrentZone());

        // Set Phong
        if (zone != null) {
            Phong currPhong = zone.getPhong(this.getPhongID());
            if (currPhong != null) {
                currPhong.outPhong(this);
            }
        }

        Vector<Room> joinedRoom = this.getJoinedRooms();
        long matchID; // Find match
        if (joinedRoom.size() > 0) {
            Room temp = joinedRoom.lastElement();
            matchID = temp.getRoomId();
            business = msgFactory.getBusiness(MessagesID.MATCH_CANCEL);
            CancelRequest rqMatchCancel = (CancelRequest) msgFactory.getRequestMessage(MessagesID.MATCH_CANCEL);
            rqMatchCancel.uid = uid;
            rqMatchCancel.mMatchId = matchID;
            rqMatchCancel.isLogout = true;

            try {
                business.handleMessage(this, rqMatchCancel, responsePkg);
            } catch (ServerException se) {
                this.mLog.error("[Netty Socket] Exception Catch Error!", se.getCause());
            }

        }

    }

    private int currRoomID = 0;
    private int roomLevel = 0;
    private int phongID = 0;

    @Override
    public void setAvatarNull() {
        avatarString = "";
    }

    @Override
    public void addAvatarString(String temp) {
        this.lastPing = System.currentTimeMillis();
        avatarString = avatarString + temp;
        mLog.debug(" avatar length" + avatarString.length());
    }

    @Override
    public String getAvatarString() {
        return avatarString;
    }

    private String avatarString = "";

    @Override
    public int getPhongID() {
        return phongID;
    }

    @Override
    public void setPhongID(int phongID) {
        this.phongID = phongID;
    }

    @Override
    public int getRoomLevel() {
        return roomLevel;
    }

    @Override
    public void setRoomLevel(int roomLevel) {
        this.roomLevel = roomLevel;
    }

    @Override
    public int getRoomID() {
        return this.currRoomID;
    }

    @Override
    public void setRoomID(int id) {
        this.currRoomID = id;

    }

    private int currentZone = -1;

    @Override
    public int getCurrentZone() {
        return currentZone;
    }

    @Override
    public void setScreenSize(String screen) {
        this.screenSize = screen;
    }

    @Override
    public void setMobile(String ver) {
        mobileVersion = ver;
        isMobile = true;
    }

    @Override
    public int getByteProtocol() {
        return protocolVersion;
    }

    @Override
    public void setByteProtocol(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    long lastPing = 0;

    @Override
    public long getLastMessage() {
        return lastMessage;
    }

    @Override
    public boolean realExpired() {
        if (lastMessage > 0 && (System.currentTimeMillis() - lastMessage) > (Integer) getAttribute("session.timeout")) {
            return true;
        }
        return false;
    }

    public String remoteIP = "";

    @Override
    public void setIP(String ip1) {

        remoteIP = ip1;
        try {
            String[] ipElements = remoteIP.split(":");
            ip = ipElements[0];
        } catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);
        }
    }

    @Override
    public String getIP() {
        return remoteIP;
    }

    @Override
    public boolean realDead() {
        try {
            if (lastPing != 0 && (lastPing - lastMessage) > DEAD_TIMEOUT) {
                // find dead session in table and put it into database
                if (isLoggedIn()) {
                    // TODO: NamNT remove dead session
                    // mLog.warn("Real dead [userName]" + getUserName() +
                    // "[Zone] " +
                    // getRoomID() + "[phongId]" + getPhongID());
                    // UserDB userDB = new UserDB();
                    // userDB.insertDeadSession(getUID(), getPhongID());

                    cancelTable();
                }

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private long lastMessage = 0;

    @Override
    public void receiveMessage() {
        lastMessage = System.currentTimeMillis();
    }

    @Override
    public void ping(ISession owner) {
        // KeepConnectionRequest k;
        // KeepConnectionResponse;
        // System.out.println("Name Owner : " + owner.getUserName() + "  ;  "
        // + owner.getUID());
        // System.out.println("This : " + getUserName() + "  ;  " + getUID());
        // MessageFactory msgFactory = owner.getMessageFactory();
        MessageFactory msgFactory = getMessageFactory();
        // KeepConnectionResponse k = new KeepConnectionResponse();
        KeepConnectionResponse k = (KeepConnectionResponse) msgFactory.getResponseMessage(MessagesID.KEEP_CONNECTION);

        // GetPokerResponse k = (GetPokerResponse)
        // msgFactory.getResponseMessage(MessagesID.GET_POKER);
        try {
            if (k == null) {
                //System.out.println("Errror :(");
            }

            this.write(k);
            if (!realDead()) {
                lastPing = System.currentTimeMillis();
            }
           
        } catch (ServerException ex) {
            java.util.logging.Logger.getLogger(AbstractSession.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public boolean getMobile() {
        return isMobile;
    }

    @Override
    public String getMobileVer() {
        return mobileVersion;
    }

    @Override
    public void setCurrentZone(int currentZone) {
        this.currentZone = currentZone;
    }

    // End
    protected void setAttribute(String aKey, Object aValue) {
        if (aValue == null) {
            this.mAttrs.remove(aKey);
        } else {
            this.mAttrs.put(aKey, aValue);
        }
    }

    protected Object getAttribute(String aKey) {
        Object value = this.mAttrs.get(aKey);
        return value;
    }

    void setID(String aId) {
        String sId = (String) getAttribute("session.id");
        if (sId != null) {
            getManager().removeSession(sId);

            AbstractSession existedSession = (AbstractSession) getManager().removeSession(aId);
            if ((existedSession != null) && (!(existedSession.isClosed()))) {
                this.mAttrs.clear();
                this.mAttrs.putAll(existedSession.mAttrs);

                existedSession.doClose();
            }
        }

        setAttribute("session.id", aId);

        getManager().addSession(aId, this);
    }

    @Override
    public String getID() {
        String sessionId = (String) getAttribute("session.id");
        return sessionId;
    }

    @Override
    public void setUID(Long aId) {
        getManager().addUIDSession(aId, this);

        setAttribute("session.user.id", aId);
    }

    @Override
    public void setUIDNull() {
        setAttribute("session.user.id", null);
    }

    @Override
    public Long getUID() {
        Long uid = (Long) getAttribute("session.user.id");
        if (uid == null) {
            uid = Long.valueOf(0L);
        }
        return uid;
    }

    @Override
    public void setUserName(String aUserName) {
        setAttribute("session.user.name", aUserName);
    }

    @Override
    public String getUserName() {
        String userName = (String) getAttribute("session.user.name");
        return userName;
    }

    void setManager(SessionManager aSessionMgr) {
        this.mSessionMgr = aSessionMgr;
    }

    @Override
    public SessionManager getManager() {
        return this.mSessionMgr;
    }

    @Override
    public void setBusinessProperties(BusinessProperties aBusinessProps) {
        setAttribute("session.business.props", aBusinessProps);
    }

    @Override
    public BusinessProperties getBusinessProperties() {
        BusinessProperties businessProps = (BusinessProperties) getAttribute("session.business.props");
        return businessProps;
    }

    boolean isSpam = false;

    @Override
    public void setSpam(boolean is) {
        isSpam = is;
    }

    @Override
    public boolean isSpam() {
        return isSpam;
    }

    @Override
    public synchronized void close() {
        if (!(isClosed())) {
            // long uid = this.getUID();
            // DatabaseDriver.updateUserOnline(uid, false);

            String id = getID();
            getManager().sessionClosed(id);

            doClose();

            this.mLog.debug("[SESSION] Session Closed: " + id);
        }
    }

    private void doClose() {
        this.mIsClosed = true;

        BusinessProperties businessProps = getBusinessProperties();
        if (businessProps != null) {
            businessProps.freeResources();
        }

        this.mAttrs.clear();
    }

    @Override
    public String userInfo() {
        // String s = "";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (this.isMobile) {
            sb.append("Mobile:Ver-").append(mobileVersion).append(":").append(screenSize).append(":");
        } // s = s + "Mobile:Ver-" + mobileVersion + ":" + screenSize + ":";
        else {
            sb.append("Flash:");
        }
        // s = s + "Flash:";
        sb.append(getUserName()).append("][").append(getIP()).append("][").append(getUID()).append("-").append(getCurrentZone()).append("]");
        // s = s + getUserName() + "][" + getIP() + "][" + getUID() + "-"
        // + getCurrentZone();
        // return "[" + s + "]";
        return sb.toString();
    }

    @Override
    public synchronized void sessionClosed() {
        if (!(isClosed())) {
            if ((isDirect()) || (isExpired())) {
                close();
            } else {
                setAttribute("session.attachment.object", null);
            }
        }
    }

    @Override
    public boolean isClosed() {
        return this.mIsClosed;
    }

    @Override
    public void sessionCreated(Object aAttachmentObj) {
        setAttribute("session.attachment.object", aAttachmentObj);

        this.mIsClosed = false;
    }

    @Override
    public Object getProtocolOutput() {
        return getAttribute("session.attachment.object");
    }

    @Override
    public boolean write(Object aObj) throws ServerException {
        //System.out.println("vaoday write");
        if (realDead()) {
            // mLog.info(this.getUserName() + " : write to RealDead");
            return false;
        }

        synchronized (this.mResPkg) {
            if (aObj instanceof IResponseMessage) {
               
//                 int msgID = ((IResponseMessage) aObj).getID();
//                 //add by zep
//                 if(msgID != 12006 && msgID != 12005 && msgID != 12009 && msgID != 12004 && msgID != 2000 && msgID != 1004){
//                    //System.out.println("vaoday setLastAccessTime:"+msgID);
//                    setLastAccessTime(Calendar.getInstance().getTime());
//
//                 }
//                 //end add by zep
                this.mResPkg.addMessage((IResponseMessage) aObj);
            }/*
			 * else if (aObj instanceof IResponsePackage) {
			 * this.mResPkg.addPackage((IResponsePackage) aObj); }
             */ else if (aObj instanceof String) {
                String res = (String) aObj;
                // System.out.println(" length package response" +
                // res.getBytes());
                return writeResponse(res.getBytes());
            }

            return write();
        }
    }

    @SuppressWarnings("rawtypes")
    private boolean write() throws ServerException {

        synchronized (this.mResPkg) {
            if (isDirect()) {
                if (!(isHandling())) {
                    String pkgFormat = getPackageFormat();
                    //mLog.debug("pkgFormat():"+pkgFormat);
                    MessageFactory msgFactory = getMessageFactory();
                    IPackageProtocol pkgProtocol = msgFactory.getPackageProtocol(pkgFormat);
                    IByteBuffer encodedRes = pkgProtocol.encode(this, this.mResPkg);
                    //mLog.debug("encodedRes():"+encodedRes.getString());
                    if (encodedRes != null) {
                        //mLog.debug("getIsSetLastAccessTime():"+getIsSetLastAccessTime() +" getUserName:"+this.getUserName());
//                        if(getIsSetLastAccessTime()){
//                            setLastAccessTime(Calendar.getInstance().getTime());
//                        }

                        byte[] resData = encodedRes.array();

                        return writeResponse(resData);
                    }
                    // return true;
                }
                return true;
            }

            Vector directMsgs = this.mResPkg.optAllMessages();
            directMsgs.clear();
            return true;
        }
    }

    protected abstract boolean writeResponse(byte[] paramArrayOfByte) throws ServerException;

    @Override
    public IResponsePackage getDirectMessages() {
        return this.mResPkg;
    }

    @Override
    public void setIsHandling(Boolean aIsHandling) {
        if (realDead()) {
            mLog.info(this.getUserName() + " : setIsHandling to RealDead");
        }

        synchronized (this.mResPkg) {
            setAttribute("session.is.handling", aIsHandling);
        }
    }

    @Override
    public boolean isHandling() {
        synchronized (this.mResPkg) {
            Boolean result = (Boolean) getAttribute("session.is.handling");
            if (result == null) {
                result = Boolean.valueOf(false);
            }
            return result.booleanValue();
        }
    }

    @Override
    public String getCookies() {
        String result = (String) getAttribute("session.cookies");
        return result;
    }

    @Override
    public void setCookies(String aCookies) {
        setAttribute("session.cookies", aCookies);
    }

    @Override
    public MessageFactory getMessageFactory() {
        MessageFactory msgFactory = (MessageFactory) getAttribute("session.message.factory");
        return msgFactory;
    }

    @Override
    public void setMessageFactory(MessageFactory aMsgFactory) {
        setAttribute("session.message.factory", aMsgFactory);
    }

    @Override
    public String getPackageFormat() {
        String pkgFormat = (String) getAttribute("session.package.format");
        return pkgFormat;
    }

    @Override
    public void setPackageFormat(String aPkgFormat) {
        setAttribute("session.package.format", aPkgFormat);
    }

    @Override
    public Date getCreatedTime() {
        Date createdTime = (Date) getAttribute("session.created.time");
        return createdTime;
    }

    @Override
    public void setCreatedTime(Date aCreatedTime) {
        setAttribute("session.created.time", aCreatedTime);
    }

    @Override
    public Date getLastAccessTime() {
        Date lastAccessTime = (Date) getAttribute("session.lastaccess.time");
        return lastAccessTime;
    }

    @Override
    public void setLastAccessTime(Date aLastAccessTime) {
        setAttribute("session.lastaccess.time", aLastAccessTime);
    }

    @Override
    public void setTimeout(Integer aMiliSeconds) {
        setAttribute("session.timeout", aMiliSeconds);
    }

    @Override
    public boolean isExpired() {
        Date lastAccessTime = getLastAccessTime();
        Date createdTime = getCreatedTime();
        
        if (lastAccessTime == null) {
            lastAccessTime = getCreatedTime();
            if (lastAccessTime == null) {

                return false;
            }
        }

        Integer timeout = (Integer) getAttribute("session.timeout");
        if (timeout == null) {
            timeout = Integer.valueOf(0);
        }

        long lastTimeout = lastAccessTime.getTime() + timeout.intValue();

        long now = Calendar.getInstance().getTimeInMillis();
//        mLog.debug("createdTime:"+createdTime.getTime());
//        mLog.debug("lastAccessTime.getTime():"+lastAccessTime.getTime());
//        mLog.debug("timeout.intValue():"+timeout.intValue());
//        mLog.debug("now:"+now);
        long checkLastCreatedTime = (now - createdTime.getTime()) / 1000;
        //mLog.debug("checkLastCreatedTime:"+checkLastCreatedTime);
//        if(checkLastCreatedTime <= 300 ){//nho hon 5P thi cho cho choi tiep
//            //mLog.debug("duoi 9000:");
//            return false;
//        }

        return (lastTimeout < now);
    }

    @Override
    public void setLoggedIn(Boolean aIsLoggedIn) {
        setAttribute("session.logged.in", aIsLoggedIn);
    }

    @Override
    public boolean isLoggedIn() {
        Boolean isLoggedIn = (Boolean) getAttribute("session.logged.in");
        if (isLoggedIn == null) {
            isLoggedIn = Boolean.FALSE;
        }
        return isLoggedIn.booleanValue();
    }

    /*
	 * public void setCurrentDBConnection(IConnection aConn) {
	 * setAttribute("session.current.db.connection", aConn); }
	 * 
	 * public IConnection getCurrentDBConnection() { IConnection result =
	 * (IConnection)getAttribute("session.current.db.connection"); return
	 * result; }
     */
    @Override
    public void setCommit(boolean aIsCommit) {
        setAttribute("session.is.commit", Boolean.valueOf(aIsCommit));
    }

    @Override
    public boolean isCommit() {
        Boolean result = (Boolean) getAttribute("session.is.commit");
        if (result == null) {
            result = Boolean.FALSE;
        }
        return result.booleanValue();
    }

    @Override
    public void joinedRoom(Room aRoom) {
        if ((aRoom != null) && (aRoom.getRoomId() > 0L)) {
            synchronized (this.mJoinedRooms) {
                this.mJoinedRooms.put(Long.valueOf(aRoom.getRoomId()), aRoom);
            }
        }
    }

    @Override
    public Room findJoinedRoom(long aRoomId) {
        synchronized (this.mJoinedRooms) {
            return ((Room) this.mJoinedRooms.get(Long.valueOf(aRoomId)));
        }
    }

    @Override
    public Room leftRoom(long aRoomId) {
        synchronized (this.mJoinedRooms) {
            return ((Room) this.mJoinedRooms.remove(Long.valueOf(aRoomId)));
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Vector<Room> getJoinedRooms() {
        Enumeration eRooms;
        synchronized (this.mJoinedRooms) {
            eRooms = this.mJoinedRooms.elements();
        }
        Vector joinedRooms = new Vector();
        while (eRooms.hasMoreElements()) {
            Room aRoom = (Room) eRooms.nextElement();
            joinedRooms.add(aRoom);
        }
        return joinedRooms;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean isJoinedFull(int aZoneId) {
        int joinedZone = 0;

        Enumeration eRooms;
        synchronized (this.mJoinedRooms) {
            eRooms = this.mJoinedRooms.elements();
        }
        while (eRooms.hasMoreElements()) {
            Room aRoom = (Room) eRooms.nextElement();
            if (aRoom.getZone().getZoneId() == aZoneId) {
                ++joinedZone;
            }
        }

        Zone zone = findZone(aZoneId);
        return ((zone.getJoinLimited() != -1) && (zone.getJoinLimited() <= joinedZone));
    }

    @Override
    public void setZoneManager(ZoneManager aZoneMgr) {
        this.mZoneMgr = aZoneMgr;
    }

    @Override
    public Zone findZone(int aZoneId) {
        return this.mZoneMgr.findZone(aZoneId);
    }

    @Override
    public Room getRoom() {
        return room;
    }

    @Override
    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * @return the collectInfo
     */
    @Override
    public StringBuilder getCollectInfo() {
        return collectInfo;
    }

    @Override
    public long getLastFastMatch() {
        return lastFastMatchId;
    }

    @Override
    public void setLastFastMatch(long lastFastMatchId) {
        this.lastFastMatchId = lastFastMatchId;
    }

    /**
     * @return the mobileDevice
     */
    @Override
    public boolean isMobileDevice() {
        return mobileDevice;
    }

    /**
     * @param mobileDevice the mobileDevice to set
     */
    @Override
    public void setMobileDevice(boolean mobileDevice) {
        this.mobileDevice = mobileDevice;
    }

    @Override
    public int getChatRoom() {
        return chatRoomId;
    }

    @Override
    public void setChatRoom(int chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    @Override
    public boolean isUploadAvatar() {
        return uploadAvatar;
    }

    @Override
    public void setUploadAvatar(boolean isUpload) {
        this.uploadAvatar = isUpload;
    }

    private boolean isHidden = false;

    @Override
    public void setHide(boolean b) {
        isHidden = b;
    }

    @Override
    public boolean isHidden() {
        return isHidden;
    }

    private boolean isMxh = false;

    @Override
    public void setMXHDevice(boolean b) {
        isMxh = b;
    }

    @Override
    public boolean isMXHDevice() {
        return isMxh;
    }

    @Override
    public UserEntity getUserEntity() {
        return this.userEntity;
    }

    @Override
    public void setUserEntity(UserEntity entity) {
        this.userEntity = entity;

    }

    private boolean acceptInvite = true;

    @Override
    public void setAcceptInvite(boolean b) {
        this.acceptInvite = b;
    }

    @Override
    public boolean isAcceptInvite() {
        return acceptInvite;
    }

    private InviteEntity inviteEnity; // avoid conflict invitation

    @Override
    public void setInviteEntity(InviteEntity entity) {
        this.inviteEnity = entity;
    }

    @Override
    public InviteEntity getInviteEntity() {
        return this.inviteEnity;
    }

    private boolean rejectInvite;

    @Override
    public void setRejectInvite(boolean rejectInvite) {
        this.rejectInvite = rejectInvite;
    }

    @Override
    public boolean isRejectInvite() {
        return rejectInvite;
    }

    @Override
    public void setLastRegister(long lastRegister) {
        this.lastRegister = lastRegister;
    }

    @Override
    public long getLastRegister() {
        return this.lastRegister;
    }

    private long lastFP;

    @Override
    public void setLastFP(long lastFP) {
        this.lastFP = lastFP;
    }

    @Override
    public long getLastFP() {
        return lastFP;
    }

    private boolean replyInvite = true;

    @Override
    public void setReplyInvite(boolean b) {
        this.replyInvite = b;
    }

    @Override
    public boolean isReplyInvite() {
        return this.replyInvite;
    }

    private int numInvite = 0;

    @Override
    public int getNumInvite() {
        return numInvite;
    }

    @Override
    public void setNumInvite(int numInvite) {
        this.numInvite = numInvite;
    }

    private FindFriendEntity findFriendCache;

    @Override
    public FindFriendEntity getFindFriendCache() {
        return findFriendCache;
    }

    @Override
    public void setFindFriendCache(FindFriendEntity entity) {
        this.findFriendCache = entity;
    }

    private UploadAvatarEntity uploadEntity;

    @Override
    public UploadAvatarEntity getUploadAvatarEntity() {
        return uploadEntity;
    }

    @Override
    public void setUploadAvatarEntity(UploadAvatarEntity uploadEntity) {
        this.uploadEntity = uploadEntity;
    }

    private byte[] uploadPart;

    @Override
    public byte[] getUploadBytePart() {
        return uploadPart;
    }

    @Override
    public void setUploadBytePart(byte[] part) {
        this.uploadPart = part;
    }

    private PositionEntity currPosition = new PositionEntity();

    @Override
    public PositionEntity getCurrPosition() {
        return currPosition;
    }

    @Override
    public void setCurrPosition(PositionEntity currPosition) {
        this.currPosition = currPosition;
    }

    private List<RetryLoginEntity> lstRetries = new ArrayList<>();

    @Override
    public int getRetryLogin(String userName) {
        int retrySize = lstRetries.size();
        for (int i = 0; i < retrySize; i++) {
            RetryLoginEntity entity = lstRetries.get(i);
            if (entity.getName().equals(userName)) {

                return entity.getRetryTimes();
            }
        }

        return 0;
    }

    @Override
    public void setRetryLogin(String userName) {
        int retrySize = lstRetries.size();
        for (int i = 0; i < retrySize; i++) {
            RetryLoginEntity entity = lstRetries.get(i);
            if (entity.getName().equals(userName)) {
                entity.setRetryTimes(entity.getRetryTimes() + 1);
                return;
            }
        }

        RetryLoginEntity entity = new RetryLoginEntity(userName, 1);
        lstRetries.add(entity);
    }

    private int botType = 0;
    
    @Override
    public int getBotType() {
        return botType;

    }

    @Override
    public void setBotType(int botType) {
        this.botType = botType;
    }
    
  

    private long lastSendImage = 0;

    @Override
    public long getLastSendImage() {
        return lastSendImage;
    }

    @Override
    public void setLastSendImage(long lastSendImage) {
        this.lastSendImage = lastSendImage;
    }

    private int deviceType = 0;

    @Override
    public int getDeviceType() {
        return deviceType;

    }

    @Override
    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    private UUID imageRequest = null;

    @Override
    public UUID getImageRequest() {
        return imageRequest;
    }

    @Override
    public void setImageRequest(UUID imageRequest) {
        this.imageRequest = imageRequest;
    }

    private long lastUid;

    public void setLastUID(long uuid) {
        lastUid = uuid;
    }

    public long getLastUID() {
        return lastUid;
    }

    public int getTopplayVersion() {
        return topplayVersion;
    }

    public void setTopplayVersion(int topplayVersion) {
        this.topplayVersion = topplayVersion;
    }
    //add by zep

    @Override
    public void setUserManager(LoginRequest entity) {
        if (entity != null) {
            this.userManager = entity;
        }
    }

    @Override
    public LoginRequest getUserManager() {
        return this.userManager;
    }
    //xu dung cho tai xiu
    @Override
    public void setTaiXiuDayThang(long daythang) {
        this.txDayThang = daythang;
    }
    @Override
    public void setTaiXiuDayThua(long daythua) {
        this.txDayThua = daythua;
    }
    @Override
    public long getTaiXiuDayThang() {
        return this.txDayThang;
    }
    @Override
    public long getTaiXiuDayThua() {
       return this.txDayThua;
    }
    @Override
    public void setTaiXiuTotalBet(long bet) {
        this.txTotalBet = bet;
    }
    @Override
    public long getTaiXiuTotalBet() {
        return this.txTotalBet;
    }
    @Override
    public boolean setIsSetLastAccessTime(boolean is) {
        return this.setLastAccessTime = is;
    }
    @Override
    public boolean getIsSetLastAccessTime() {
       return this.setLastAccessTime;
    }
    @Override
    public void setuType(int uType) {
        this.uType = uType;
    }
    @Override
    public int getuType() {
        return this.uType;

    }
   @Override
    public void setWaitRecharge(int WaitRecharge) {
        this.isWaitRecharge = WaitRecharge;
    }
    @Override
    public int getWaitRecharge() {
        return this.isWaitRecharge;

    }
    @Override
    public void setRealMoney(String type) {
        this.isRealMoney = type;
        if(type.equals("realmoney")){
            this.setIsRealMoney(true);
        }else{
            this.setIsRealMoney(false);
        }
    }
    @Override
    public String getRealMoney() {
        return this.isRealMoney;

    }
    
    @Override
    public void setIsRealMoney(boolean type) {
        this.isBRealMoney = type;
    }
    @Override
    public boolean getIsRealMoney() {
        return this.isBRealMoney;

    }
    //end add by zep

}
