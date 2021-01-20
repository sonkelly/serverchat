package vn.game.room;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.IResponseMessage;
import vn.game.session.ISession;
import allinone.data.SimpleTable;

public class Room {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(Room.class);
    public int mCapacity;
    private int mPlayerLimit;
    private long mRoomId;
    private String mRoomName;
    private final ConcurrentHashMap<Long, ISession> mEnteringSessions;
    private final ConcurrentHashMap<Long, ISession> mPlayingSessions;
    
    private SimpleTable mAttactmentData;
    private boolean mIsPermanent;
    private Zone mZone;
    private int realmoney;//0 tien xu, 1 tien that
    //@ Binhlt - begin
   public ConcurrentHashMap<Long, ISession> getEnteringSession(){
//	   synchronized (mEnteringSessions) {
		   return this.mEnteringSessions;
//	}
	   
   }
   
    public void setZone(Zone mZone) {
        this.mZone = mZone;
    }

    public Zone getZone() {
        return this.mZone;
    }
    
    public int index;
    public int getIndex() {
		return index;
	}
    public void setIndex(int index) {
		this.index = index;
	}
    public int phongID;
    public void setPhongID(int phongID) {
		this.phongID = phongID;
	}
    public int getPhongID() {
		return phongID;
	}
    private String ownerName;

    public String getOwnerName() {
        return ownerName;
    }
    public boolean setNewOner(){
        return true;
    }
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    private int zoneID;

    public void setZoneID(int zoneID) {
        this.zoneID = zoneID;
    }

    public int getZoneID() {
        return zoneID;
    }
    public void setRealMoney(int _realmoney) {
        this.realmoney = _realmoney;
    }

    public int getRealMoney() {
        return realmoney;
    }
    
    
    private final ConcurrentHashMap<Long, ISession> mWaitingSessions;
    private String password = null;
    private int level = 0;
    private boolean isPlaying = false;

    public String getPassword() {
        return password;
    }

    public int getNumOnline()
    {
        int count =0;
        Enumeration<ISession>e = mPlayingSessions.elements();
        for(; e.hasMoreElements(); ) { 
            
            ISession is=e.nextElement();
            if (is!=null && !is.isClosed())
                count++;

        }
        
        return count;
        
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean checkPassword(String input) {
        if (this.password == null) {
            return true;
        }
        if ((input != null)
                && (this.password.compareTo(input) == 0)) {
            return true;
        } else {
            return false;
        }
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setPlayerSize(int size) {
        this.mPlayerLimit = size;
    }

    public ISession getWaitingSessionByID(long id) {
//        synchronized (this.mWaitingSessions) {
            return this.mWaitingSessions.get(id);
//        }
    }

    public ISession getSessionByID(long id) {
//        synchronized (this.mEnteringSessions) {
            return this.mEnteringSessions.get(id);
//        }
    }

    public ISession addWaitingSessionByID(ISession session) {
        synchronized (this.mWaitingSessions) {
            return this.mWaitingSessions.put(session.getUID().longValue(), session);
        }

    }

    public void removeWaitingSessionByID(ISession session) {
        synchronized (this.mWaitingSessions) {
            this.mWaitingSessions.remove(session);
        }
    }
    //@ Binhlt - end

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Room(int aCapacity, int aPlayerLimit, Zone aZone) {
        this.mCapacity = aCapacity;
        this.mPlayerLimit = aPlayerLimit;
        this.mEnteringSessions = new ConcurrentHashMap(this.mCapacity);
        this.mPlayingSessions = new ConcurrentHashMap(this.mCapacity);
        this.mWaitingSessions = new ConcurrentHashMap<Long, ISession>();
        this.mIsPermanent = false;

        this.mZone = aZone;
    }

   

    public void setRoomId(long aRoomId) {
        this.mRoomId = aRoomId;
    }

    public long getRoomId() {
        return this.mRoomId;
    }

    public void setName(String aRoomName) {
        if ((this.mRoomName == null) || (!(this.mIsPermanent))) {
            this.mRoomName = aRoomName;
        }
    }

    public String getName() {
        return this.mRoomName;
    }

    void setPermanent() {
        this.mIsPermanent = true;
    }

    public boolean isPermanent() {
        return this.mIsPermanent;
    }

    public void setAttachmentData(SimpleTable aAttactmentData) {
        this.mAttactmentData = aAttactmentData;
    }

    public SimpleTable getAttactmentData() {
        return this.mAttactmentData;
    }

    int enteringSize() {
        
            return this.mEnteringSessions.size();
        
    }

    int playingSize() {
        //synchronized (this.mEnteringSessions) {
            return this.mPlayingSessions.size();
        //}
    }

    boolean isEmpty() {
        synchronized (this.mEnteringSessions) {
            return this.mEnteringSessions.isEmpty();
        }
    }

    public void joinPlayer(ISession aSession) {
        synchronized (this.mPlayingSessions) {
            long uid = aSession.getUID().longValue();
            this.mPlayingSessions.put(Long.valueOf(uid), aSession);
        }
    }

    public int joinRoom(ISession aSession) {
        synchronized (this.mEnteringSessions) {
            long uid = aSession.getUID().longValue();
            if (!this.mEnteringSessions.contains(Long.valueOf(uid))) {
                this.mEnteringSessions.put(Long.valueOf(uid), aSession);
                aSession.joinedRoom(this);
                return 1;
            } else {
                return 2;
            }
        }
    }

    public int join(ISession aSession) {
        synchronized (this.mEnteringSessions) {
            long uid = aSession.getUID().longValue();
            if (this.mEnteringSessions.contains(Long.valueOf(uid))) {
                return 4;
            }
            if (enteringSize() < this.mCapacity) {
                this.mEnteringSessions.put(Long.valueOf(uid), aSession);
                aSession.joinedRoom(this);
                if (playingSize() < this.mPlayerLimit) {
                    this.mPlayingSessions.put(aSession.getUID(), aSession);
                    return 1;
                }
                return 2;
            }

            return 3;
        }
    }

    public void left(ISession aSession) {
        
    	if (aSession == null) return;
    	
        long uid = aSession.getUID().longValue();        
        
        synchronized (this.mEnteringSessions) {
            this.mEnteringSessions.remove(Long.valueOf(uid));
            this.mPlayingSessions.remove(Long.valueOf(uid));
            this.mLog.debug("[ROOM] " + aSession.getUserName() + " has been left room " + this.mRoomName);
            aSession.leftRoom(this.mRoomId);//@ BinhLT
            if (isEmpty()) {
                this.mZone.deleteRoom(this);
            }
        }
    }

    public long getStatus() {
        synchronized (this.mEnteringSessions) {
            if (playingSize() >= this.mPlayerLimit) {
                return 1L;
            }

            return 2L;
        }
    }
    //@SuppressWarnings("unchecked")

    public void broadcastMessage(IResponseMessage aResMsg, ISession aSender, boolean aIsSendMe, int u) {
        Enumeration<ISession> enumSessions = null;
        synchronized (this.mEnteringSessions) {
            enumSessions = this.mEnteringSessions.elements();
            //this.mLog.debug("[ROOM] Room size = " + this.mEnteringSessions.size());
        }        
        while (true) {
            ISession session;
            while (true) {
                if (!(enumSessions.hasMoreElements())) {
                    return;
                }

                session = (ISession) enumSessions.nextElement();
                //System.out.println("Sesion ID : "+session.getUID()+" : "+aSender.getUID());
                if ((aIsSendMe) || (session.getUID() != aSender.getUID())) {
                    break;
                }
            }
            try {
                if (session!=null && aResMsg!=null && !session.isClosed())
                {
                    
                    session.write(aResMsg);
                }
                else
                {
                    mLog.error("Broad cast error : Isession null : room : "+ getName()+" ; " + (session==null) );
                    
                    //TODO: NamNT delete room which has null session(invalid room)
                    allLeft();
                    
                }
                //this.mLog.debug("[ROOM] Broadcast to " + session.getUserName());

            } catch (Throwable t) {
                this.mLog.error("[ROOM] Broadcast. Room : "+this.mRoomName);
                this.mLog.error("[ROOM] Broadcast error", t);
            }
        }
    }

    /**
     * BinhLT - send response to one client
     * @return
     */
    public void sendMessage(IResponseMessage aResMsg, ISession aSender, String receiverName) {
        Enumeration<ISession> enumSessions = null;
        synchronized (this.mEnteringSessions) {
            enumSessions = this.mEnteringSessions.elements();
            this.mLog.debug("[ROOM] Room size = " + this.mEnteringSessions.size());
        }
        String senderName = aSender.getUserName();
        while (true) {
            if (!(enumSessions.hasMoreElements())) {
                return;
            }
            ISession session = (ISession) enumSessions.nextElement();
            if (session.getUserName().compareTo(receiverName) == 0) {
                try {
                    session.write(aResMsg);
                    this.mLog.debug("[ROOM] Send from " + senderName + " to " + receiverName);
                    break;
                } catch (Throwable t) {
                    this.mLog.error("[ROOM] Send private message error", t);
                }
            }
        }
    }

    public void sendMessage(IResponseMessage aResMsg, String receiverName) {
        Enumeration<ISession> enumSessions = null;
        synchronized (this.mEnteringSessions) {
            ConcurrentHashMap<Long, ISession> sessions = new ConcurrentHashMap<Long, ISession>();
            sessions.putAll(this.mEnteringSessions);
            sessions.putAll(this.mWaitingSessions);
            enumSessions = sessions.elements();
            this.mLog.debug("[ROOM] Room size = " + sessions.size());
        }
        while (true) {
            if (!(enumSessions.hasMoreElements())) {
                return;
            }
            ISession session = (ISession) enumSessions.nextElement();
            if (session.getUserName().compareTo(receiverName) == 0) {
                try {
                    session.write(aResMsg);
                    this.mLog.debug("[ROOM] Send to " + receiverName);
                    break;
                } catch (Throwable t) {
                    this.mLog.error("[ROOM] Send private message error", t);
                }
            }
        }
    }
    // Kick-out all session(not close) and kill room

    public void allLeft() {
        synchronized (this.mEnteringSessions) {
            Enumeration<Long> enter = this.mEnteringSessions.keys();
            while (enter.hasMoreElements()) {
                ISession aSession = this.mEnteringSessions.get(enter.nextElement());
                //TODO: NamNT remove null session
                if(aSession != null)
                {    
                    long uid = aSession.getUID().longValue();
                    this.mEnteringSessions.remove(Long.valueOf(uid));
                    this.mPlayingSessions.remove(Long.valueOf(uid));

                    this.mLog.debug("[ROOM] " + aSession.getUserName()
                            + " has been left room " + this.mRoomName);
                    aSession.leftRoom(this.mRoomId);
                }
            }
        }
        this.mZone.deleteRoom(this);
    }
    // BinhLT end

    public RoomEntity dumpRoom() {
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.mRoomId = this.mRoomId;
        roomEntity.mRoomName = this.mRoomName;
        roomEntity.mRoomOwnerName = this.ownerName;
        roomEntity.mCapacity = this.mCapacity;
        roomEntity.mEnteringSize = enteringSize();
        roomEntity.mPlayingSize = playingSize();
        roomEntity.mAttactmentData = this.mAttactmentData;
        roomEntity.mPassword = this.password;
        return roomEntity;
    }
}
