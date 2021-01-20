package com.allinone.vivu;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.business.BusinessException;
import allinone.data.Couple;
import allinone.data.MessagesID;
import allinone.data.ResponseCode;
import allinone.protocol.messages.MovingResponse;

public class Group {

    private final ConcurrentHashMap<Long, ISession> mSessions;
    private int MAX_SESSION = 10;
    private Broadcaster worker;
    // private long TIMEOUT = 30000l;
    private int mGroupIndex;
    private Area mSubZone;
    // private long timeActivated;
    private static final Logger mLog = LoggerContext.getLoggerFactory()
            .getLogger(Group.class);

    public Logger getLogger() {
        return mLog;
    }

    public int getEnteringSession() {
        //synchronized (mSessions) {
        return mSessions.size();
        //}
    }

    public int getMAX_SESSION() {
        return MAX_SESSION;
    }

    private MessageFactory msgFactory;
    private final ConcurrentHashMap<Long, Couple<Integer, Integer>> buffers;

    public void setMsgFactory(MessageFactory msgFactory) {
        this.msgFactory = msgFactory;
    }

    public MessageFactory getMsgFactory() {
        return msgFactory;
    }

    public boolean isFull() {
        //synchronized (mSessions) {
        return (mSessions.size() >= MAX_SESSION);
        //}
    }

    public void insertBuffer(long uid, int x, int y) {
        Couple<Integer, Integer> pos = new Couple<Integer, Integer>(x, y);
        //synchronized (buffers) {
        // mLog.debug("Add moving");
        buffers.put(uid, pos);
        // mLog.debug("buffer size:" + buffers.size());
        //}
    }

    public void write() throws ServerException {
        MovingResponse resMove = (MovingResponse) msgFactory
                .getResponseMessage(MessagesID.Moving);
        boolean isWrite = false;
        //synchronized (buffers) {
        // mLog.debug("buffer size:" + buffers.size());
        Enumeration<Long> keys = buffers.keys();
        ArrayList<MovingInfo> ds = new ArrayList<MovingInfo>();
        while (keys.hasMoreElements()) {
            Long k = keys.nextElement();
            Couple<Integer, Integer> d = buffers.get(k);

            ds.add(new MovingInfo(d.e1, d.e2, k, "", "", true));
            isWrite = true;
        }
        resMove.setSuccess(ds);
        buffers.clear();
        //}
        //synchronized (mSessions) {
        if (mSessions.size() > 1 && isWrite) {
            // mLog.debug("broadcasting:" + mSessions.size());
            broadcastMoving(resMove);
        }

        //}
    }

    public ArrayList<FuckingPlayer> getUsers() throws BusinessException {
        ArrayList<FuckingPlayer> res = new ArrayList<FuckingPlayer>();
        Enumeration<ISession> sessions = getmSessions();
        while (sessions.hasMoreElements()) {
            ISession s = sessions.nextElement();
            FuckingPlayer user = s.getUser();
            if (user != null && !s.isHidden()) {
                res.add(user);
            }
        }
        return res;
    }

    public int getmGroupIndex() {
        return mGroupIndex;
    }

    public Area getmSubZone() {
        return mSubZone;
    }

    public Enumeration<ISession> getmSessions() {
        //synchronized (mSessions) {
        return mSessions.elements();
        //}
    }

    @SuppressWarnings("deprecation")
    public void join(ISession session) throws BusinessException {
        //synchronized (mSessions) {
        if (mSessions.size() < MAX_SESSION) {
            // timeActivated = System.currentTimeMillis();
            mLog.debug("Group " + this.getmGroupIndex() + " Join session:"
                    + session.getUserName());
            mSessions.put(session.getUID(), session);
            if (!worker.isRunning()) {
                worker.start();
            } else if (worker.isSuppend()) {
                worker.resume();
            }
        } else {
            throw new BusinessException("Limit of session!");
        }
        //}
    }

    @SuppressWarnings("deprecation")
    public void left(ISession session) throws BusinessException {
        FuckingPlayer user = session.getUser();
        long uid = user.id;
        //synchronized (mSessions) {
        mSessions.remove(Long.valueOf(uid));
        mLog.debug("Group " + this.getmGroupIndex() + " Left session:"
                + user.username);
        if (mSessions.isEmpty()) {
            worker.suspend();
            worker.setIsSuppend(true);
        }
        //}
    }

    public void broadcast(Object obj) throws ServerException {
        Enumeration<ISession> sessions = getmSessions();
        ArrayList<ISession> needRemoveSession = new ArrayList<ISession>();
        while (sessions.hasMoreElements()) {
            ISession s = sessions.nextElement();
            if (s != null) {
                try {
                    s.write(obj);
                } catch (NullPointerException e) {
                    needRemoveSession.add(s);
                }
            }
        }
        removeSessionIdle(needRemoveSession);
    }

    public void broadcast(Object obj, int buildingID, ISession aSession)
            throws ServerException, BusinessException {
        long sessID = aSession.getUID();
        Enumeration<ISession> sessions = getmSessions();
        ArrayList<ISession> needRemoveSession = new ArrayList<ISession>();
        while (sessions.hasMoreElements()) {
            ISession s = sessions.nextElement();
            if (s != null) {
                if ((s.getBuilding().id == buildingID)
                        && (s.getUID() != sessID)
                        && (s.getUser().isSameScene(aSession.getUser()))) {
                    try {
                        s.write(obj);
                    } catch (NullPointerException e) {
                        needRemoveSession.add(s);
                    }
                }
            }
        }
        removeSessionIdle(needRemoveSession);
    }

    public MovingResponse reduceMovingData(MovingResponse data, long uid) {
        MovingResponse res = (MovingResponse) msgFactory
                .getResponseMessage(MessagesID.Moving);
        res.data = new ArrayList<MovingInfo>();
        res.mCode = ResponseCode.SUCCESS;
        for (MovingInfo m : data.data) {
            if (m.uid != uid) {
                res.data.add(m);
            }
        }
        return res;
    }

    private void removeSessionIdle(ArrayList<ISession> needRemoveSession) {
        //synchronized (mSessions) {
        for (ISession s : needRemoveSession) {
            this.mSessions.remove(s.getUID());
        }
        //}

    }

    public void broadcastMoving(MovingResponse data) throws ServerException {
        Enumeration<ISession> sessions = getmSessions();
        ArrayList<ISession> needRemoveSession = new ArrayList<ISession>();
        while (sessions.hasMoreElements()) {
            ISession s = sessions.nextElement();
            if (s == null || s.getGroup() == null || s.realDead() || s.getGroup().getmGroupIndex() != mGroupIndex || s.isHidden()) {
                needRemoveSession.add(s);
                continue;
            }

            if (s != null && !s.isHidden()) {
                MovingResponse red = reduceMovingData(data, s.getUID());
                if (!red.data.isEmpty()) {
                    try {
                        s.write(red);
                    } catch (NullPointerException e) {
                        needRemoveSession.add(s);
                    }
                }
            }
        }
        removeSessionIdle(needRemoveSession);
    }

    public void broadcast(Object obj, boolean isSendSender, long sessID)
            throws ServerException {
         mLog.debug("vaoday0");
        Enumeration<ISession> sessions = getmSessions();
         mLog.debug("vaoday123");
        ArrayList<ISession> needRemoveSession = new ArrayList<ISession>();
        while (sessions.hasMoreElements()) {
            mLog.debug("vaoday");
            ISession s = sessions.nextElement();
            if (s == null || s.getGroup() == null || s.realDead() || s.getGroup().getmGroupIndex() != mGroupIndex || s.isHidden()) {
                needRemoveSession.add(s);
                continue;
            }

            if (s != null && !s.isHidden()) {
                try {
                    if (s.getUID() == sessID) {
                        if (isSendSender) {
                            mLog.debug("vaoday1");
                            s.write(obj);
                        }
                    } else {
                        mLog.debug("vaoday");
                        s.write(obj);
                    }
                } catch (NullPointerException e) {
                    //e.printStackTrace();
                    needRemoveSession.add(s);
                }
            }
        }
        removeSessionIdle(needRemoveSession);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Group(int index, Area sZ) {
        mSessions = new ConcurrentHashMap(MAX_SESSION);
        buffers = new ConcurrentHashMap(MAX_SESSION);
        // timeActivated = System.currentTimeMillis();
        mGroupIndex = index;
        mSubZone = sZ;
        worker = new Broadcaster(this);
        // worker.start();
    }
}

class Broadcaster extends Thread {

    private Group group;
    private boolean isRunning = false;
    private boolean isSuppend = false;

    public void setIsSuppend(boolean isSuppend) {
        this.isSuppend = isSuppend;
    }

    public boolean isSuppend() {
        return isSuppend;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public Broadcaster(Group g) {
        this.group = g;
    }

    @Override
    public void run() {
        group.getLogger().debug("Thread writing is running!");
        this.isRunning = true;
        while (true) {
            try {
                sleep(500);

                // group.getLogger().debug("Write running");
                group.write();
            } catch (InterruptedException ex) {
            } catch (ServerException se) {
            }

        }
    }
}
