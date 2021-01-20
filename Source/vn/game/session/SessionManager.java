package vn.game.session;

//import com.punch.framework.common.ILoggerFactory;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import tools.CacheUserInfo;
import vn.game.common.LoggerContext;
import vn.game.db.DBException;
import vn.game.room.Room;
import allinone.data.UserEntity;
import allinone.databaseDriven.FriendDB;
import allinone.databaseDriven.SessionDB;
import allinone.databaseDriven.TaiXiuDayThangThuaDB;
import allinone.databaseDriven.UserDB;

public class SessionManager {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(SessionManager.class);
    private final ConcurrentHashMap<String, ISession> mSessions;
    private final ConcurrentHashMap<Long, String> mUIDSessions;
    private final ConcurrentHashMap<Long, ISession> prvSessions;
    private final Vector<ISessionListener> mSessionListeners;
    private IdGenerator mIdGenerator;
    private int mSessionTimeout = -1;
    public boolean shutDown = false;
    private String isRealMoney = "realmoney";
    @SuppressWarnings({"unchecked", "rawtypes", "unused"})
    public SessionManager(int aSessionTimeout) {
        this.mSessions = new ConcurrentHashMap();
        this.mUIDSessions = new ConcurrentHashMap();

        this.mSessionTimeout = aSessionTimeout;
        this.prvSessions = new ConcurrentHashMap<Long, ISession>();

       

        if (this.mSessionTimeout > 0) {
            // CloseTimeoutSessionScheduler sScheduler = new
            // CloseTimeoutSessionScheduler(this, null);
            CloseTimeoutSessionScheduler sScheduler = new CloseTimeoutSessionScheduler();
            Thread tScheduler = new Thread(sScheduler);
            // tScheduler.start();
        }

        this.mIdGenerator = new IdGenerator();

        this.mSessionListeners = new Vector();
    }

    public ConcurrentHashMap<String, ISession> getmSessions() {
        return mSessions;
    }

    public void sessionCreated(ISession aSession) {
        ((AbstractSession) aSession).setManager(this);

        mLog.debug("Number connection [Add session]" + this.mSessions.size());

        String nextId = this.mIdGenerator.generateId();
        ((AbstractSession) aSession).setID(nextId);
        mLog.debug(" [Add session] ID " + nextId);

        Long nextUID = Long.valueOf(this.mIdGenerator.generateUID());
        mLog.debug(" [Add session] UID " + nextUID);

        aSession.setUID(nextUID);
        aSession.setTimeout(Integer.valueOf(this.mSessionTimeout));
        aSession.setRealMoney(this.isRealMoney);
    }

    public void addSession(String aId, ISession aSession) {

        if (aId.equals(aSession.getID())) {
            synchronized (this.mSessions) {
                this.mSessions.put(aId, aSession);

                this.mUIDSessions.put(aSession.getUID(), aId);
            }
        } else {
            ((AbstractSession) aSession).setID(aId);
        }
    }

    void addUIDSession(Long aUid, ISession aSession) {

        if (aUid.longValue() > 0L) {
            Long uid = aSession.getUID().longValue();
            if (uid < 0) {
                aSession.setLastUID(uid);
            }
            this.mUIDSessions.remove(Long.valueOf(uid));

            this.mUIDSessions.put(aUid, aSession.getID());
        } else {
            this.mUIDSessions.put(aUid, aSession.getID());
        }

        if (aUid.longValue() > 0L) {
            Long uid = aSession.getUID().longValue();
            synchronized (this.mSessions) {
                this.mUIDSessions.remove(Long.valueOf(uid));

                this.mUIDSessions.put(aUid, aSession.getID());
            }
        } else {
            synchronized (this.mSessions) {
                this.mUIDSessions.put(aUid, aSession.getID());
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<ISession> findAllSession(long aId) {
        List<ISession> values = new ArrayList(this.mSessions.values());
        int sessionSize = values.size();
        List<ISession> results = new ArrayList<ISession>();
        for (int i = 0; i < sessionSize; i++) {
            AbstractSession ses = (AbstractSession) values.get(i);
            long sessUid = (Long) ses.getAttribute("session.user.id");
            if (sessUid == aId) {
                results.add(ses);
            }
        }
        return results;

    }

    public ISession findPrvChatSession(Long aUid) {
        if (prvSessions.contains(aUid)) {
            ISession newSession = prvSessions.get(aUid);
            if (newSession != null) {
                return newSession;
            }
            // return prvSessions.get(aUid);
        }

        ISession newSession = findSession(aUid);
        if (newSession != null) {
            prvSessions.put(aUid, newSession);
        }

        return newSession;
    }

    public void removePrvChatSession(Long aUid) {
        prvSessions.remove(aUid);
    }

    public ISession findSession(String aId) {
        // synchronized (this.mSessions) {
        if (this.mSessions.containsKey(aId)) {
            return ((ISession) this.mSessions.get(aId));
        }

        return null;
        // }
    }

    public ISession findSession(Long aUid) {

        // synchronized (this.mSessions) {
        String sessionId = (String) this.mUIDSessions.get(aUid);
        if (sessionId != null) {
            mLog.debug("sessionId:"+sessionId);
            return findSession(sessionId);
        }

        // }
        return null;
    }

    public ISession removeSession(String aId) {
        synchronized (this.mSessions) {
            ISession session = (ISession) this.mSessions.remove(aId);
            if (session != null) {
                this.mUIDSessions.remove(session.getUID());
            }
            return session;
        }
    }

    public ISession removeSession(ISession aSession) {
        synchronized (this.mSessions) {
            if (aSession != null) {
                this.mUIDSessions.remove(aSession.getUID());

                String id = aSession.getID();
                return ((ISession) this.mSessions.remove(id));
            }

            return null;
        }
    }

    @SuppressWarnings("rawtypes")
    public ISession sessionClosed(String aId) {
        ISession session = null;

        session = (ISession) this.mSessions.remove(aId);
        boolean scanAll = true;
        long rmId = 0;
        if (session != null) {
            rmId = session.getUID();
            String sessionId = this.mUIDSessions.get(rmId);
            if (sessionId != null && sessionId.equals(aId)) {
                scanAll = false;

            } else {
                rmId = session.getLastUID();
                sessionId = this.mUIDSessions.get(rmId);
                if (sessionId != null && sessionId.equals(aId)) {
                    scanAll = false;
                }
            }

        }

        if (scanAll) {
            //mLog.error(new StringBuilder("Fuck session omg! ").append(rmId).append("_").append(aId).toString());
           
            

            // Iterator itr = mUIDSessions.keys().;
            // iterate through HashMap values iterator
            Enumeration t = mUIDSessions.keys();

            Object foundItem = null;
            while (t.hasMoreElements()) {
                Object l = t.nextElement();
                 //mLog.debug(l+" : "+mUIDSessions.get(l));
                if (mUIDSessions.get(l).equalsIgnoreCase(aId)) {
                    foundItem = l;
                }
            }
            if (foundItem != null) {
                mUIDSessions.remove(foundItem, aId);
            }

        } else {
            this.mUIDSessions.remove(rmId);
            notifySessionClosed(session);
        }

        try {

//            if (!scanAll && rmId > 0 && session.isLoggedIn()) {
//                UserDB userDb = new UserDB();
//                userDb.logout(rmId, session.getCollectInfo().toString());
//            }
//            try {
//                SessionDB sdb = new SessionDB();
//                sdb.updateSessionDB(session.getUID(), "", "", "", "");
//            } catch (Exception e) {
//
//            }
            //luu day thang day thua tx
//            long daythang = session.getTaiXiuDayThang();
//            long daythua = session.getTaiXiuDayThua();
//            long totalBet = session.getTaiXiuTotalBet();
//            if(daythang > 0 || daythua > 0 && rmId > 0){
//                TaiXiuDayThangThuaDB logDuDayObj = new TaiXiuDayThangThuaDB(); 
//                logDuDayObj.addLog(rmId, session.getUserEntity().viewName, daythang, daythua,session.getuType(),totalBet);
//            }
            //end luu day thang day thua tx
            //mLog.debug(new StringBuilder("Number connection ").append(this.mSessions.size()).toString());
        } catch (Exception ex) {
            mLog.debug(new StringBuilder("session manager eror: ").append(ex.getMessage()).toString());
        }
        return session;
    }

    public void addSessionListener(ISessionListener aSessionListener) {
        synchronized (this.mSessionListeners) {
            this.mSessionListeners.add(aSessionListener);
        }
    }

    @SuppressWarnings("rawtypes")
    private void notifySessionClosed(ISession aSession) {
        synchronized (this.mSessionListeners) {
            Iterator it = this.mSessionListeners.iterator();
            while (true) {
                if (!(it.hasNext())) {
                    break;
                }
                ISessionListener sessionListener = (ISessionListener) it.next();
                sessionListener.sessionClosed(aSession);
            }
        }
    }

    private class CloseTimeoutSessionScheduler implements Runnable {

        @SuppressWarnings("rawtypes")
        public void run() {
            System.out.println("Run : CloseTimeoutSessionScheduler");

            Enumeration values;
            try {
                values = null;

                synchronized (SessionManager.this.mSessions) {
                    values = SessionManager.this.mSessions.elements();
                }

                // System.out.println("values : "+(values==null));
                while (true) {
                    // System.out.println("values : "+(values==null)+" ; "+SessionManager.this.mSessions.values().size());
                    if ((values != null) && (SessionManager.this.mSessions.values().size() > 0)) {

                        for (ISession session : SessionManager.this.mSessions.values()) {
                            if (shutDown) {
                                session.close();
                            }

                            // ISession session = (ISession)
                            // values.nextElement();
                            // System.out.println("Came here : "+session.getUID());
                            // if ((!(session.isClosed())) &&
                            // (session.isExpired()|| session.realExpired() )) {
                            // if ((!(session.isClosed())) &&
                            // (session.isExpired()|| session.realExpired() )) {
                            if ((!(session.isClosed())) && (session.isExpired())) {
                                // SessionCloseThead sThread = new
                                // SessionCloseThead(session, session);
                                // sThread.start();
                                mLog.info("Close idle session : " + session.userInfo());
                                // BINHLT
                                session.cancelTable();
                                session.close();
                            }
                        }
                    }
                    // Thread.sleep(5000L);
                }

            } catch (Throwable t) {
                // SessionManager.access$200(this.this$0).error("", t);
                SessionManager.this.mLog.error("", t);
            }
        }

        @SuppressWarnings("unused")
        private class SessionCloseThead extends Thread {

            private final ISession mSession;

            public SessionCloseThead(ISession aSession, ISession paramISession) {
                this.mSession = aSession;
            }

            public void run() {
                synchronized (this.mSession) {
                    // SessionManager.access$200(this.this$1.this$0).debug("[SCHEDULER] Session "
                    // + this.mSession.getID() +
                    // " has been timeout, closed it!");
                    SessionManager.this.mLog.debug("[SCHEDULER] Session " + this.mSession.getID() + " has been timeout, closed it!");
                    this.mSession.close();
                }
            }
        }
    }

    // Binhlt
    public void removeUIDSession(long uid) {
        synchronized (this.mUIDSessions) {
            this.mUIDSessions.remove(uid);
        }
    }

    // TODO: NamNT get free user in all zone
    public Vector<UserEntity> dumpFreeUsers(int limitSize) {
        Vector<UserEntity> users = new Vector<UserEntity>();
        Enumeration<Long> keys = null;

        synchronized (this.mUIDSessions) {
            keys = this.mUIDSessions.keys();

        }
        int results = 0;
        // UserDB userDb = new UserDB();
        while (true) {
            if ((results >= limitSize) || (!keys.hasMoreElements())) {
                break;
            }
            long uid = keys.nextElement();

            try {
                ISession session = findSession(uid);

                if (session != null) {

                    Vector<Room> joinedRoom = session.getJoinedRooms();
                    if (joinedRoom.size() == 0) {
                        CacheUserInfo cacheUser = new CacheUserInfo();
                        UserEntity user = cacheUser.getUserInfo(uid,session.getRealMoney());
                        users.add(user);
                        ++results;
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                mLog.error(ex.getMessage(), ex);
            }

        }

        return users;
    }

    public Vector<UserEntity> dumpFreeFriend(int aLength, int aLevel, int currentZone, long minimumMoneyJoin,ISession sessionPlayerInvite) throws DBException, SQLException {

        Vector<UserEntity> userEntities = new Vector<UserEntity>();

        try {

            Enumeration<Long> keys = this.mUIDSessions.keys();

            int results = 0;

            while (true) {

                if ((results >= aLength) || (!keys.hasMoreElements())) {
                    break;
                }

                long uid = keys.nextElement();

                try {

                    ISession session = findSession(uid);

                    if (session != null) {

                        Vector<Room> joinedRoom = session.getJoinedRooms();

                        if (joinedRoom.isEmpty() && session.getUID() != 0 && session.isLoggedIn() // && (session.getCurrentZone() <= 0 ||
                                // session.getCurrentZone() == currentZone)
                                // && !session.isRejectInvite()
                                // && session.isReplyInvite()
                                ) {

                            CacheUserInfo cacheUser = new CacheUserInfo();
                            UserEntity user = cacheUser.getUserInfo(uid,session.getRealMoney());

                            if (user != null && user.mUid > 0 && user.money >= minimumMoneyJoin && user.utype == 0 && sessionPlayerInvite.getRealMoney().equals(session.getRealMoney())) {
                                userEntities.add(user);
                                ++results;
                            }
                        }
                    }
                } catch (Exception ex) {
                    mLog.error(" Get free friendslist error " + ex.getMessage());
                    ex.printStackTrace();
                }
            }

        } finally {

        }

        return userEntities;
    }
    public Vector<UserEntity> getListFriend(int aLength, int aLevel, int currentZone, long minimumMoneyJoin) throws DBException, SQLException {

        Vector<UserEntity> userEntities = new Vector<UserEntity>();

        try {

            Enumeration<Long> keys = this.mUIDSessions.keys();

            int results = 0;

            while (true) {

                if ((results >= aLength) || (!keys.hasMoreElements())) {
                    break;
                }

                long uid = keys.nextElement();

                try {

                    ISession session = findSession(uid);

                    if (session != null) {

                        Vector<Room> joinedRoom = session.getJoinedRooms();

                        if (joinedRoom.isEmpty() && session.getUID() != 0 && session.isLoggedIn() ) {

                            CacheUserInfo cacheUser = new CacheUserInfo();
                            UserEntity user = cacheUser.getUserInfo(uid,session.getRealMoney());

                            if (user != null && user.mUid > 0 && user.money >= minimumMoneyJoin && user.utype == 0) {
                                // get is friend
                                 int isFriendStatus = new FriendDB().isFriend(uid, user.mUid);
                                 if(isFriendStatus == 1){// la friend thi add 
                                    userEntities.add(user);
                                    ++results;
                                 }
                            }
                        }
                    }
                } catch (Exception ex) {
                    mLog.error(" Get free friendslist error " + ex.getMessage());
                    ex.printStackTrace();
                }
            }

        } finally {

        }

        return userEntities;
    }
}
